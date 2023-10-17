package br.com.souza.twitterclone.directmessages.handler.websocket;

import br.com.souza.twitterclone.directmessages.configuration.authorization.TokenProvider;
import br.com.souza.twitterclone.directmessages.util.SingletonDmChatsConnections;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class DmChatsHandler extends TextWebSocketHandler {

    private final SingletonDmChatsConnections singletonDmChatsConnections;
    private final TokenProvider tokenProvider;

    public DmChatsHandler(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
        this.singletonDmChatsConnections = SingletonDmChatsConnections.getInstance();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Optional<String> sessionToken = sessionToken(session);

        if (sessionToken.isPresent() && tokenProvider.validateTokenWebSocketSession(sessionToken.get())) {
            String identifier = tokenProvider.getIdentifierFromToken(sessionToken.get());
            singletonDmChatsConnections.put(identifier, session);
        } else {
            session.close(CloseStatus.POLICY_VIOLATION);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Optional<String> sessionToken = sessionToken(session);
        if (sessionToken.isPresent() && tokenProvider.validateTokenWebSocketSession(sessionToken.get())) {
            String identifier = tokenProvider.getIdentifierFromToken(sessionToken.get());
            Set<WebSocketSession> sessions = singletonDmChatsConnections.get(identifier);
            for (WebSocketSession s : sessions){
                s.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Optional<String> sessionToken = sessionToken(session);
        if (sessionToken.isPresent() && tokenProvider.validateTokenWebSocketSession(sessionToken.get())) {
            String identifier = tokenProvider.getIdentifierFromToken(sessionToken.get());
            singletonDmChatsConnections.remove(identifier, session);
        }
        session.close(CloseStatus.SERVER_ERROR);
    }

    private Optional<String> sessionToken(WebSocketSession session) {
        return Optional
                .ofNullable(session.getUri())
                .map(UriComponentsBuilder::fromUri)
                .map(UriComponentsBuilder::build)
                .map(UriComponents::getQueryParams)
                .map(it -> it.get("token"))
                .flatMap(it -> it.stream().findFirst())
                .map(String::trim);
    }
}