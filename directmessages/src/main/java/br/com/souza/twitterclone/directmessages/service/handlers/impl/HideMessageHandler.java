package br.com.souza.twitterclone.directmessages.service.handlers.impl;

import br.com.souza.twitterclone.directmessages.client.IAccountsClient;
import br.com.souza.twitterclone.directmessages.client.IFeedClient;
import br.com.souza.twitterclone.directmessages.configuration.authorization.TokenProvider;
import br.com.souza.twitterclone.directmessages.database.model.ChatIgnoredMessages;
import br.com.souza.twitterclone.directmessages.database.model.ChatIgnoredMessagesId;
import br.com.souza.twitterclone.directmessages.database.model.ChatMessages;
import br.com.souza.twitterclone.directmessages.database.repository.IChatIgnoredMessagesRepository;
import br.com.souza.twitterclone.directmessages.database.repository.IChatMessagesRepository;
import br.com.souza.twitterclone.directmessages.dto.ChatsMessageResponse;
import br.com.souza.twitterclone.directmessages.dto.MessageRequest;
import br.com.souza.twitterclone.directmessages.service.handlers.IMessageHandlerStrategy;
import br.com.souza.twitterclone.directmessages.service.handlers.factory.MessageHandlerFactory;
import br.com.souza.twitterclone.directmessages.util.SingletonChatMessagesConnections;
import br.com.souza.twitterclone.directmessages.util.SingletonDmChatsConnections;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class HideMessageHandler implements IMessageHandlerStrategy {

    private final IChatMessagesRepository iChatMessagesRepository;
    private final IChatIgnoredMessagesRepository iChatIgnoredMessagesRepository;
    private final SingletonDmChatsConnections singletonDmChatsConnections;
    private final SingletonChatMessagesConnections singletonChatMessagesConnections;
    private final TokenProvider tokenProvider;
    private final IAccountsClient iAccountsClient;
    private final IFeedClient iFeedClient;
    private final ObjectMapper objectMapper;

    public HideMessageHandler(IChatMessagesRepository iChatMessagesRepository,
                              IChatIgnoredMessagesRepository iChatIgnoredMessagesRepository,
                              TokenProvider tokenProvider,
                              IAccountsClient iAccountsClient,
                              IFeedClient iFeedClient,
                              ObjectMapper objectMapper) {
        this.iChatMessagesRepository = iChatMessagesRepository;
        this.iChatIgnoredMessagesRepository = iChatIgnoredMessagesRepository;
        this.tokenProvider = tokenProvider;
        this.iAccountsClient = iAccountsClient;
        this.iFeedClient = iFeedClient;
        this.objectMapper = objectMapper;
        this.singletonDmChatsConnections = SingletonDmChatsConnections.getInstance();
        this.singletonChatMessagesConnections = SingletonChatMessagesConnections.getInstance();
    }

    @Override
    public void processMessage(MessageRequest messageRequest, String chatIdentifier, String sessionUserIdentifier, String receiverIdentifier, String sessionToken) throws Exception {
        Set<WebSocketSession> chatMessagesSessions = singletonChatMessagesConnections.get(chatIdentifier);
        Set<WebSocketSession> sessionUserSessions = singletonDmChatsConnections.get(sessionUserIdentifier);
        Set<WebSocketSession> secondaryUserSessions = singletonDmChatsConnections.get(receiverIdentifier);

        ChatMessages chatMessage = iChatMessagesRepository.findById(messageRequest.getMessageIdentifier())
                .orElseThrow(() -> new Exception("Message not found"));

        iChatIgnoredMessagesRepository.save(ChatIgnoredMessages.builder()
                .id(ChatIgnoredMessagesId.builder()
                        .messageIdentifier(chatMessage.getIdentifier())
                        .chatIdentifier(chatMessage.getChatIdentifier())
                        .userIdentifier(receiverIdentifier)
                        .build())
                .build());

        if (chatMessagesSessions != null) {
            for (WebSocketSession s : chatMessagesSessions) {
                s.sendMessage(createHideMessage(s, sessionToken, chatMessage));
            }
        }
        if (sessionUserSessions != null) {
            for (WebSocketSession s : sessionUserSessions) {
                s.sendMessage(new TextMessage("Updated"));
            }
        }
        if (secondaryUserSessions != null) {
            for (WebSocketSession s : secondaryUserSessions) {
                s.sendMessage(new TextMessage("Updated"));
            }
        }
    }

    private String getSessionUserIdentifier(WebSocketSession session) throws IOException {
        Optional<String> sessionToken = sessionToken(session);
        if (sessionToken.isPresent() && tokenProvider.validateTokenWebSocketSession(sessionToken.get())) {
            return tokenProvider.getIdentifierFromToken(sessionToken.get());
        } else {
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

    public TextMessage createHideMessage(WebSocketSession session, String sessionToken, ChatMessages chatMessage) throws Exception {
        String chatMessagesSessionsUserIdentifier = getSessionUserIdentifier(session);
        ChatsMessageResponse sessionUserChatResponse = new ChatsMessageResponse(chatMessage, iFeedClient, iAccountsClient, sessionToken, chatMessagesSessionsUserIdentifier, "HIDE_MESSAGE");
        String sessionUserResponse = objectMapper.writeValueAsString(sessionUserChatResponse);
        return new TextMessage(sessionUserResponse);
    }
}
