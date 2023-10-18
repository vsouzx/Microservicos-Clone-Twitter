package br.com.souza.twitterclone.directmessages.service.commons.impl;

import br.com.souza.twitterclone.directmessages.configuration.authorization.TokenProvider;
import br.com.souza.twitterclone.directmessages.database.model.DmChats;
import br.com.souza.twitterclone.directmessages.service.commons.IHandlersCommons;
import java.io.IOException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Service
public class HandlersCommonsImpl implements IHandlersCommons {

    private final TokenProvider tokenProvider;

    public HandlersCommonsImpl(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public String getReceiverIdentifier(DmChats chat, String sessionUserIdentifier) {
        return chat.getUserIdentifier1().equals(sessionUserIdentifier) ? chat.getUserIdentifier2() : chat.getUserIdentifier1();
    }

    @Override
    public Optional<String> sessionToken(WebSocketSession session) {
        return Optional
                .ofNullable(session.getUri())
                .map(UriComponentsBuilder::fromUri)
                .map(UriComponentsBuilder::build)
                .map(UriComponents::getQueryParams)
                .map(it -> it.get("token"))
                .flatMap(it -> it.stream().findFirst())
                .map(String::trim);
    }

    @Override
    public Optional<String> getChatIdentifier(WebSocketSession session) {
        return Optional
                .ofNullable(session.getUri())
                .map(UriComponentsBuilder::fromUri)
                .map(UriComponentsBuilder::build)
                .map(UriComponents::getQueryParams)
                .map(it -> it.get("chatIdentifier"))
                .flatMap(it -> it.stream().findFirst())
                .map(String::trim);
    }

    @Override
    public String getSessionUserIdentifier(WebSocketSession session) throws IOException {
        Optional<String> sessionToken = sessionToken(session);
        if (sessionToken.isPresent() && tokenProvider.validateTokenWebSocketSession(sessionToken.get())) {
            return tokenProvider.getIdentifierFromToken(sessionToken.get());
        } else {
            session.close(CloseStatus.BAD_DATA);
            return null;
        }
    }
}
