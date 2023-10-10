package br.com.souza.twitterclone.directmessages.handler.websocket;

import br.com.souza.twitterclone.directmessages.configuration.authorization.TokenProvider;
import br.com.souza.twitterclone.directmessages.database.model.ChatMessages;
import br.com.souza.twitterclone.directmessages.database.model.DmChats;
import br.com.souza.twitterclone.directmessages.database.repository.IChatMessagesRepository;
import br.com.souza.twitterclone.directmessages.database.repository.IDmChatsRepository;
import br.com.souza.twitterclone.directmessages.util.SingletonChatMessagesConnections;
import br.com.souza.twitterclone.directmessages.util.SingletonDmChatsConnections;
import br.com.souza.twitterclone.directmessages.util.UsefulDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ChatMessagesHandler extends TextWebSocketHandler {

    private final SingletonChatMessagesConnections singletonChatMessagesConnections;
    private final SingletonDmChatsConnections singletonDmChatsConnections;
    private final TokenProvider tokenProvider;
    private final IDmChatsRepository iDmChatsRepository;
    private final IChatMessagesRepository iChatMessagesRepository;

    public ChatMessagesHandler(TokenProvider tokenProvider,
                               IDmChatsRepository iDmChatsRepository,
                               IChatMessagesRepository iChatMessagesRepository) {
        this.iDmChatsRepository = iDmChatsRepository;
        this.iChatMessagesRepository = iChatMessagesRepository;
        this.singletonChatMessagesConnections = SingletonChatMessagesConnections.getInstance();
        this.singletonDmChatsConnections = SingletonDmChatsConnections.getInstance();
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Optional<String> sessionToken = sessionToken(session);

        if (sessionToken.isPresent() && tokenProvider.validateTokenWebSocketSession(sessionToken.get())) {
            System.out.println("Conexão validada");
            Optional<String> chatIdentifier = getChatIdentifier(session);
            if (chatIdentifier.isPresent()) {
                singletonChatMessagesConnections.put(chatIdentifier.get(), session);
            } else {
                session.close(CloseStatus.BAD_DATA);
                System.out.println("BAD_DATA");
            }
        } else {
            session.close(CloseStatus.POLICY_VIOLATION);
            System.out.println("POLICY_VIOLATION");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.printf("mensagem recebida");
        Optional<String> sessionToken = sessionToken(session);
        if (sessionToken.isPresent() && tokenProvider.validateTokenWebSocketSession(sessionToken.get())) {
            String userIdentifier = tokenProvider.getIdentifierFromToken(sessionToken.get());
            Optional<String> chatIdentifier = getChatIdentifier(session);
            if (chatIdentifier.isPresent()) {
                Optional<DmChats> chat = iDmChatsRepository.findById(chatIdentifier.get());
                if (chat.isEmpty()) {
                    session.close(CloseStatus.BAD_DATA);
                    return;
                }

                Set<WebSocketSession> chatMessagesSessions = singletonChatMessagesConnections.get(chatIdentifier.get());
                Set<WebSocketSession> sessionUserSessions = singletonDmChatsConnections.get(userIdentifier);
                Set<WebSocketSession> userIdentifier2Sessions = singletonDmChatsConnections.get(chat.get().getUserIdentifier2());

                iChatMessagesRepository.save(ChatMessages.builder()
                        .identifier(UUID.randomUUID().toString())
                        .chatIdentifier(chatIdentifier.get())
                        .userIdentifier(userIdentifier)
                        .text(message.getPayload())
                        .creationDate(UsefulDate.now())
                        .seen(userIdentifier2Sessions != null && !userIdentifier2Sessions.isEmpty())
                        .build());

                System.out.println("Mensagem salva no banco");

                if(chatMessagesSessions != null){
                    for (WebSocketSession s : chatMessagesSessions) {
                        s.sendMessage(message);
                    }
                }
                if(sessionUserSessions != null) {
                    for (WebSocketSession s : sessionUserSessions) {
                        s.sendMessage(message);
                    }
                }
                if(userIdentifier2Sessions != null) {
                    for (WebSocketSession s : userIdentifier2Sessions) {
                        s.sendMessage(message);
                    }
                }
            } else {
                session.close(CloseStatus.BAD_DATA);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Optional<String> sessionToken = sessionToken(session);
        if (sessionToken.isPresent() && tokenProvider.validateTokenWebSocketSession(sessionToken.get())) {
            Optional<String> chatIdentifier = getChatIdentifier(session);
            if (chatIdentifier.isPresent()) {
                singletonChatMessagesConnections.remove(chatIdentifier.get(), session);
                System.out.println("Sessão removida");
            } else {
                session.close(CloseStatus.BAD_DATA);
            }
        }
        session.close(CloseStatus.SERVER_ERROR);
        System.out.println("Conexão FECHADA");
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

    private Optional<String> getChatIdentifier(WebSocketSession session) {
        return Optional
                .ofNullable(session.getUri())
                .map(UriComponentsBuilder::fromUri)
                .map(UriComponentsBuilder::build)
                .map(UriComponents::getQueryParams)
                .map(it -> it.get("chatIdentifier"))
                .flatMap(it -> it.stream().findFirst())
                .map(String::trim);
    }
}