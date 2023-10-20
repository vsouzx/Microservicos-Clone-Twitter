package br.com.souza.twitterclone.directmessages.service.handlers.impl;

import br.com.souza.twitterclone.directmessages.client.IAccountsClient;
import br.com.souza.twitterclone.directmessages.client.IFeedClient;
import br.com.souza.twitterclone.directmessages.database.model.ChatMessages;
import br.com.souza.twitterclone.directmessages.database.repository.IChatMessagesReactionsRepository;
import br.com.souza.twitterclone.directmessages.database.repository.IChatMessagesRepository;
import br.com.souza.twitterclone.directmessages.dto.ChatsMessageResponse;
import br.com.souza.twitterclone.directmessages.dto.MessageRequest;
import br.com.souza.twitterclone.directmessages.service.commons.IHandlersCommons;
import br.com.souza.twitterclone.directmessages.service.handlers.IMessageHandlerStrategy;
import br.com.souza.twitterclone.directmessages.util.SingletonChatMessagesConnections;
import br.com.souza.twitterclone.directmessages.util.SingletonDmChatsConnections;
import br.com.souza.twitterclone.directmessages.util.UsefulDate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class NewMessageHandler implements IMessageHandlerStrategy {

    private final IChatMessagesRepository iChatMessagesRepository;
    private final SingletonDmChatsConnections singletonDmChatsConnections;
    private final SingletonChatMessagesConnections singletonChatMessagesConnections;
    private final IAccountsClient iAccountsClient;
    private final IFeedClient iFeedClient;
    private final ObjectMapper objectMapper;
    private final IHandlersCommons iHandlersCommons;
    private final IChatMessagesReactionsRepository iChatMessagesReactionsRepository;

    public NewMessageHandler(IChatMessagesRepository iChatMessagesRepository,
                             IAccountsClient iAccountsClient,
                             IFeedClient iFeedClient,
                             ObjectMapper objectMapper,
                             IHandlersCommons iHandlersCommons,
                             IChatMessagesReactionsRepository iChatMessagesReactionsRepository) {
        this.iChatMessagesRepository = iChatMessagesRepository;
        this.iAccountsClient = iAccountsClient;
        this.iFeedClient = iFeedClient;
        this.objectMapper = objectMapper;
        this.iHandlersCommons = iHandlersCommons;
        this.iChatMessagesReactionsRepository = iChatMessagesReactionsRepository;
        this.singletonDmChatsConnections = SingletonDmChatsConnections.getInstance();
        this.singletonChatMessagesConnections = SingletonChatMessagesConnections.getInstance();
    }

    @Override
    public void processMessage(MessageRequest messageRequest, String chatIdentifier, String sessionUserIdentifier, String receiverIdentifier, String sessionToken) throws Exception {
        Set<WebSocketSession> chatMessagesSessions = singletonChatMessagesConnections.get(chatIdentifier);
        Set<WebSocketSession> sessionUserSessions = singletonDmChatsConnections.get(sessionUserIdentifier);
        Set<WebSocketSession> secondaryUserSessions = singletonDmChatsConnections.get(receiverIdentifier);

        ChatMessages chatMessage = iChatMessagesRepository.save(ChatMessages.builder()
                .identifier(UUID.randomUUID().toString())
                .chatIdentifier(chatIdentifier)
                .userIdentifier(sessionUserIdentifier)
                .text(messageRequest.getTextMessage())
                .creationDate(UsefulDate.now())
                .seen(isUser2LoggedIn(chatMessagesSessions, receiverIdentifier))
                .build());

        if (chatMessagesSessions != null) {
            for (WebSocketSession s : chatMessagesSessions) {
                s.sendMessage(createNewMessage(s, sessionToken, chatMessage));
            }
        }
        if (sessionUserSessions != null) {
            for (WebSocketSession s : sessionUserSessions) {
                s.sendMessage(new TextMessage("New message"));
            }
        }
        if (secondaryUserSessions != null) {
            for (WebSocketSession s : secondaryUserSessions) {
                s.sendMessage(new TextMessage("New message"));
            }
        }
    }

    private TextMessage createNewMessage(WebSocketSession session, String sessionToken, ChatMessages chatMessage) throws Exception {
        String chatMessagesSessionsUserIdentifier = iHandlersCommons.getSessionUserIdentifier(session);
        ChatsMessageResponse sessionUserChatResponse = new ChatsMessageResponse(chatMessage, iFeedClient, iAccountsClient, sessionToken, chatMessagesSessionsUserIdentifier, "NEW_MESSAGE", iChatMessagesReactionsRepository);
        String sessionUserResponse = objectMapper.writeValueAsString(sessionUserChatResponse);
        return new TextMessage(sessionUserResponse);
    }

    private Boolean isUser2LoggedIn(Set<WebSocketSession> chatMessagesSessions, String receiverIdentifier) throws Exception {
        boolean isLogado = false;
        if (chatMessagesSessions != null) {
            for (WebSocketSession s : chatMessagesSessions) {
                String sessionId = iHandlersCommons.getSessionUserIdentifier(s);
                if (sessionId != null && sessionId.equals(receiverIdentifier)) {
                    isLogado = true;
                }
            }
        }
        return isLogado;
    }
}
