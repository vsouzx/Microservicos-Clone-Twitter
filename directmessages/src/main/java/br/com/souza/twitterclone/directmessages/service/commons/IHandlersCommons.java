package br.com.souza.twitterclone.directmessages.service.commons;

import br.com.souza.twitterclone.directmessages.database.model.DmChats;
import java.io.IOException;
import java.util.Optional;
import org.springframework.web.socket.WebSocketSession;

public interface IHandlersCommons {

    String getReceiverIdentifier(DmChats chat, String sessionUserIdentifier);

    Optional<String> sessionToken(WebSocketSession session);

    Optional<String> getChatIdentifier(WebSocketSession session);

    String getSessionUserIdentifier(WebSocketSession session) throws Exception;
}
