package br.com.souza.twitterclone.directmessages.handler.websocket;

import br.com.souza.twitterclone.directmessages.configuration.authorization.TokenProvider;
import br.com.souza.twitterclone.directmessages.service.commons.IHandlersCommons;
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
    private final TokenProvider tokenProvider;
    private final IHandlersCommons iHandlersCommons;

    public DmChatsHandler(TokenProvider tokenProvider,
                          IHandlersCommons iHandlersCommons) {
        this.tokenProvider = tokenProvider;
        this.iHandlersCommons = iHandlersCommons;
        this.singletonDmChatsConnections = SingletonDmChatsConnections.getInstance();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Optional<String> sessionToken = iHandlersCommons.sessionToken(session);

        if (sessionToken.isPresent() && tokenProvider.validateTokenWebSocketSession(sessionToken.get())) {
            String identifier = tokenProvider.getIdentifierFromToken(sessionToken.get());
            if (identifier != null) {
                singletonDmChatsConnections.put(identifier, session);
                session.sendMessage(new TextMessage("pong"));
                return;
            }
        }
        session.close(CloseStatus.POLICY_VIOLATION);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Optional<String> sessionToken = iHandlersCommons.sessionToken(session);
        if (sessionToken.isPresent() && tokenProvider.validateTokenWebSocketSession(sessionToken.get())) {
            String identifier = tokenProvider.getIdentifierFromToken(sessionToken.get());

            if(message.getPayload().equals("ping")){
                session.sendMessage(new TextMessage("pong"));
                return;
            }

            Set<WebSocketSession> sessions = singletonDmChatsConnections.get(identifier);
            for (WebSocketSession s : sessions) {
                s.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Optional<String> sessionToken = iHandlersCommons.sessionToken(session);
        if (sessionToken.isPresent() && tokenProvider.validateTokenWebSocketSession(sessionToken.get())) {
            String identifier = tokenProvider.getIdentifierFromToken(sessionToken.get());
            if(identifier != null){
                singletonDmChatsConnections.remove(identifier, session);
            }
        }
        session.close(CloseStatus.SERVER_ERROR);
    }
}