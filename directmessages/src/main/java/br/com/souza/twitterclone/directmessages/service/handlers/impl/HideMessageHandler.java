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
import br.com.souza.twitterclone.directmessages.service.commons.IHandlersCommons;
import br.com.souza.twitterclone.directmessages.service.handlers.IMessageHandlerStrategy;
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

@Service
public class HideMessageHandler implements IMessageHandlerStrategy {

    private final IChatMessagesRepository iChatMessagesRepository;
    private final IChatIgnoredMessagesRepository iChatIgnoredMessagesRepository;
    private final SingletonDmChatsConnections singletonDmChatsConnections;
    private final SingletonChatMessagesConnections singletonChatMessagesConnections;
    private final IAccountsClient iAccountsClient;
    private final IFeedClient iFeedClient;
    private final ObjectMapper objectMapper;
    private final IHandlersCommons iHandlersCommons;

    public HideMessageHandler(IChatMessagesRepository iChatMessagesRepository,
                              IChatIgnoredMessagesRepository iChatIgnoredMessagesRepository,
                              IAccountsClient iAccountsClient,
                              IFeedClient iFeedClient,
                              ObjectMapper objectMapper,
                              IHandlersCommons iHandlersCommons) {
        this.iChatMessagesRepository = iChatMessagesRepository;
        this.iChatIgnoredMessagesRepository = iChatIgnoredMessagesRepository;
        this.iAccountsClient = iAccountsClient;
        this.iFeedClient = iFeedClient;
        this.objectMapper = objectMapper;
        this.iHandlersCommons = iHandlersCommons;
        this.singletonDmChatsConnections = SingletonDmChatsConnections.getInstance();
        this.singletonChatMessagesConnections = SingletonChatMessagesConnections.getInstance();
    }

    @Override
    public void processMessage(MessageRequest messageRequest, String chatIdentifier, String sessionUserIdentifier, String receiverIdentifier, String sessionToken) throws Exception {
        Set<WebSocketSession> chatMessagesSessions = singletonChatMessagesConnections.get(chatIdentifier);
        Set<WebSocketSession> sessionUserSessions = singletonDmChatsConnections.get(sessionUserIdentifier);
        Set<WebSocketSession> secondaryUserSessions = singletonDmChatsConnections.get(receiverIdentifier);

        try {
            ChatMessages chatMessage = iChatMessagesRepository.findById(messageRequest.getMessageIdentifier())
                    .orElseThrow(() -> new Exception("Message not found"));

            iChatIgnoredMessagesRepository.save(ChatIgnoredMessages.builder()
                    .id(ChatIgnoredMessagesId.builder()
                            .messageIdentifier(chatMessage.getIdentifier())
                            .chatIdentifier(chatMessage.getChatIdentifier())
                            .userIdentifier(sessionUserIdentifier)
                            .build())
                    .build());

            if (chatMessagesSessions != null) {
                for (WebSocketSession s : chatMessagesSessions) {
                    try {
                        s.sendMessage(createHideMessage(s, sessionToken, chatMessage, sessionUserIdentifier));
                    } catch (Exception e) {
                        s.close();
                    }
                }
            }
            if (sessionUserSessions != null) {
                for (WebSocketSession s : sessionUserSessions) {
                    try {
                        s.sendMessage(new TextMessage("Updated"));
                    } catch (Exception e) {
                        s.close();
                    }
                }
            }
            if (secondaryUserSessions != null) {
                for (WebSocketSession s : secondaryUserSessions) {
                    try {
                        s.sendMessage(new TextMessage("Updated"));
                    } catch (Exception e) {
                        s.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro");
        }
    }

    private TextMessage createHideMessage(WebSocketSession session, String sessionToken, ChatMessages chatMessage, String sessionUserIdentifier) throws Exception {
        String chatMessagesSessionUserIdentifier = iHandlersCommons.getSessionUserIdentifier(session);
        ChatsMessageResponse sessionUserChatResponse = new ChatsMessageResponse(chatMessage, iFeedClient, iAccountsClient, sessionToken, chatMessagesSessionUserIdentifier, sessionUserIdentifier, "HIDE_MESSAGE");
        String sessionUserResponse = objectMapper.writeValueAsString(sessionUserChatResponse);
        return new TextMessage(sessionUserResponse);
    }
}
