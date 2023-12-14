package br.com.souza.twitterclone.directmessages.handler.websocket;

import br.com.souza.twitterclone.directmessages.service.commons.IHandlersCommons;
import br.com.souza.twitterclone.directmessages.service.redis.RedisService;
import br.com.souza.twitterclone.directmessages.util.SingletonDmChatsConnections;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class DmChatsHandler extends TextWebSocketHandler {

    private final SingletonDmChatsConnections singletonDmChatsConnections;
    private final IHandlersCommons iHandlersCommons;
    private final RedisService redisService;

    public DmChatsHandler(IHandlersCommons iHandlersCommons,
                          RedisService redisService) {
        this.iHandlersCommons = iHandlersCommons;
        this.redisService = redisService;
        this.singletonDmChatsConnections = SingletonDmChatsConnections.getInstance();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Optional<String> sessionToken = iHandlersCommons.sessionToken(session);

        if (sessionToken.isPresent()) {
            try {
                redisService.isValidUser(sessionToken.get());
            } catch (Exception e) {
                session.close(CloseStatus.POLICY_VIOLATION);
            }
            singletonDmChatsConnections.put(sessionToken.get(), session);
            session.sendMessage(new TextMessage("pong"));
            return;
        }
        session.close(CloseStatus.POLICY_VIOLATION);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Optional<String> sessionToken = iHandlersCommons.sessionToken(session);
        if (sessionToken.isPresent()) {
            try {
                redisService.isValidUser(sessionToken.get());
            } catch (Exception e) {
                session.close(CloseStatus.POLICY_VIOLATION);
            }

            if (message.getPayload().equals("ping")) {
                session.sendMessage(new TextMessage("pong"));
                return;
            }

            Set<WebSocketSession> sessions = singletonDmChatsConnections.get(sessionToken.get());
            for (WebSocketSession s : sessions) {
                s.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Optional<String> sessionToken = iHandlersCommons.sessionToken(session);
        if (sessionToken.isPresent()) {
            try {
                redisService.isValidUser(sessionToken.get());
            } catch (Exception e) {
                session.close(CloseStatus.POLICY_VIOLATION);
            }
            singletonDmChatsConnections.remove(sessionToken.get(), session);
        }
        session.close(CloseStatus.SERVER_ERROR);
    }
}