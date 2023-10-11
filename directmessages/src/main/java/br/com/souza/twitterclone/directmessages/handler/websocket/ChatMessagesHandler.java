package br.com.souza.twitterclone.directmessages.handler.websocket;

import br.com.souza.twitterclone.directmessages.client.IAccountsClient;
import br.com.souza.twitterclone.directmessages.client.IFeedClient;
import br.com.souza.twitterclone.directmessages.configuration.authorization.TokenProvider;
import br.com.souza.twitterclone.directmessages.database.model.ChatMessages;
import br.com.souza.twitterclone.directmessages.database.model.DmChats;
import br.com.souza.twitterclone.directmessages.database.repository.IChatMessagesRepository;
import br.com.souza.twitterclone.directmessages.database.repository.IDmChatsRepository;
import br.com.souza.twitterclone.directmessages.dto.ChatsMessageResponse;
import br.com.souza.twitterclone.directmessages.util.SingletonChatMessagesConnections;
import br.com.souza.twitterclone.directmessages.util.SingletonDmChatsConnections;
import br.com.souza.twitterclone.directmessages.util.UsefulDate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
    private final IAccountsClient iAccountsClient;
    private final IFeedClient iFeedClient;
    private final ObjectMapper mapper;

    public ChatMessagesHandler(TokenProvider tokenProvider,
                               IDmChatsRepository iDmChatsRepository,
                               IChatMessagesRepository iChatMessagesRepository,
                               IAccountsClient iAccountsClient,
                               IFeedClient iFeedClient,
                               ObjectMapper mapper) {
        this.iDmChatsRepository = iDmChatsRepository;
        this.iChatMessagesRepository = iChatMessagesRepository;
        this.iAccountsClient = iAccountsClient;
        this.iFeedClient = iFeedClient;
        this.mapper = mapper;
        this.singletonChatMessagesConnections = SingletonChatMessagesConnections.getInstance();
        this.singletonDmChatsConnections = SingletonDmChatsConnections.getInstance();
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Optional<String> sessionToken = sessionToken(session);

        if (sessionToken.isPresent() && tokenProvider.validateTokenWebSocketSession(sessionToken.get())) {
            Optional<String> chatIdentifier = getChatIdentifier(session);
            if (chatIdentifier.isPresent()) {
                singletonChatMessagesConnections.put(chatIdentifier.get(), session);
            } else {
                session.close(CloseStatus.BAD_DATA);
            }
        } else {
            session.close(CloseStatus.POLICY_VIOLATION);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
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

                String receiverIdentifier = getReceiverIdentifier(chat.get(), userIdentifier);

                Set<WebSocketSession> chatMessagesSessions = singletonChatMessagesConnections.get(chatIdentifier.get());
                Set<WebSocketSession> sessionUserSessions = singletonDmChatsConnections.get(userIdentifier);
                Set<WebSocketSession> userIdentifier2Sessions = singletonDmChatsConnections.get(receiverIdentifier);

                ChatMessages chatMessage = iChatMessagesRepository.save(ChatMessages.builder()
                        .identifier(UUID.randomUUID().toString())
                        .chatIdentifier(chatIdentifier.get())
                        .userIdentifier(userIdentifier)
                        .text(message.getPayload())
                        .creationDate(UsefulDate.now())
                        .seen(validarSeUser2EstaLogadoNoChat(chatMessagesSessions, receiverIdentifier))
                        .build());

                if(chatMessagesSessions != null){
                    for (WebSocketSession s : chatMessagesSessions) {
                        s.sendMessage(montarMensagem(s, sessionToken.get(), chatMessage));
                    }
                }
                if(sessionUserSessions != null) {
                    for (WebSocketSession s : sessionUserSessions) {
                        s.sendMessage(new TextMessage("New message"));
                    }
                }
                if(userIdentifier2Sessions != null) {
                    for (WebSocketSession s : userIdentifier2Sessions) {
                        s.sendMessage(new TextMessage("New message"));
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
            } else {
                session.close(CloseStatus.BAD_DATA);
            }
        }
        session.close(CloseStatus.SERVER_ERROR);
    }

    private String getSessionUserIdentifier(WebSocketSession session) throws IOException {
        Optional<String> sessionToken = sessionToken(session);
        if (sessionToken.isPresent() && tokenProvider.validateTokenWebSocketSession(sessionToken.get())) {
            return tokenProvider.getIdentifierFromToken(sessionToken.get());
        }else{
            session.close(CloseStatus.BAD_DATA);
            return null;
        }
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

    private String getReceiverIdentifier(DmChats chat, String sessionUserIdentifier){
        return chat.getUserIdentifier1().equals(sessionUserIdentifier) ? chat.getUserIdentifier2() : chat.getUserIdentifier1();
    }

    public TextMessage montarMensagem(WebSocketSession session, String sessionToken, ChatMessages chatMessage) throws Exception {
        String chatMessagesSessionsUserIdentifier = getSessionUserIdentifier(session);
        ChatsMessageResponse sessionUserChatResponse = new ChatsMessageResponse(chatMessage, iFeedClient, iAccountsClient, sessionToken, chatMessagesSessionsUserIdentifier);
        String sessionUserResponse = mapper.writeValueAsString(sessionUserChatResponse);
        return new TextMessage(sessionUserResponse);
    }

    private Boolean validarSeUser2EstaLogadoNoChat(Set<WebSocketSession> chatMessagesSessions, String receiverIdentifier) throws Exception {

        boolean isLogado = false;
        if(chatMessagesSessions != null){
            for (WebSocketSession s : chatMessagesSessions) {
                String sessionId = getSessionUserIdentifier(s);
                if(sessionId != null && sessionId.equals(receiverIdentifier)){
                    isLogado = true;
                }
            }
        }
        return isLogado;
    }
}