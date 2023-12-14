package br.com.souza.twitterclone.directmessages.service.handlers.impl;

import br.com.souza.twitterclone.directmessages.client.IAccountsClient;
import br.com.souza.twitterclone.directmessages.client.IFeedClient;
import br.com.souza.twitterclone.directmessages.database.model.ChatMessages;
import br.com.souza.twitterclone.directmessages.database.model.ChatMessagesReactions;
import br.com.souza.twitterclone.directmessages.database.model.ChatMessagesReactionsId;
import br.com.souza.twitterclone.directmessages.database.repository.IChatMessagesReactionsRepository;
import br.com.souza.twitterclone.directmessages.database.repository.IChatMessagesRepository;
import br.com.souza.twitterclone.directmessages.dto.ChatsMessageResponse;
import br.com.souza.twitterclone.directmessages.dto.MessageRequest;
import br.com.souza.twitterclone.directmessages.service.commons.IHandlersCommons;
import br.com.souza.twitterclone.directmessages.service.handlers.IMessageHandlerStrategy;
import br.com.souza.twitterclone.directmessages.util.SingletonChatMessagesConnections;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class ReactMessageHandler implements IMessageHandlerStrategy {

    private final IChatMessagesRepository iChatMessagesRepository;
    private final SingletonChatMessagesConnections singletonChatMessagesConnections;
    private final IAccountsClient iAccountsClient;
    private final IFeedClient iFeedClient;
    private final ObjectMapper objectMapper;
    private final IHandlersCommons iHandlersCommons;
    private final IChatMessagesReactionsRepository iChatMessagesReactionsRepository;

    public ReactMessageHandler(IChatMessagesRepository iChatMessagesRepository,
                               IAccountsClient iAccountsClient,
                               IFeedClient iFeedClient,
                               ObjectMapper objectMapper,
                               IHandlersCommons iHandlersCommons,
                               IChatMessagesReactionsRepository iChatMessagesReactionsRepository) {
        this.iChatMessagesRepository = iChatMessagesRepository;
        this.iChatMessagesReactionsRepository = iChatMessagesReactionsRepository;
        this.singletonChatMessagesConnections = SingletonChatMessagesConnections.getInstance();
        this.iAccountsClient = iAccountsClient;
        this.iFeedClient = iFeedClient;
        this.objectMapper = objectMapper;
        this.iHandlersCommons = iHandlersCommons;
    }

    @Override
    public void processMessage(MessageRequest messageRequest, String chatIdentifier, String sessionUserIdentifier, String receiverIdentifier) throws Exception {
        Set<WebSocketSession> chatMessagesSessions = singletonChatMessagesConnections.get(chatIdentifier);

        ChatMessages chatMessage = iChatMessagesRepository.findById(messageRequest.getMessageIdentifier())
                .orElseThrow(() -> new Exception("Message not found"));

        Optional<ChatMessagesReactions> reaction = iChatMessagesReactionsRepository.findByIdUserIdentifierAndIdMessageIdentifier(sessionUserIdentifier, chatMessage.getIdentifier());
        if (reaction.isPresent()) {
            if (reaction.get().getEmoji().equals(messageRequest.getTextMessage())) {
                iChatMessagesReactionsRepository.delete(reaction.get());
            } else {
                reaction.get().setEmoji(messageRequest.getTextMessage());
                iChatMessagesReactionsRepository.save(reaction.get());
            }
        }

        if (reaction.isEmpty()) {
            iChatMessagesReactionsRepository.save(ChatMessagesReactions.builder()
                    .id(ChatMessagesReactionsId.builder()
                            .identifier(UUID.randomUUID().toString())
                            .userIdentifier(sessionUserIdentifier)
                            .messageIdentifier(messageRequest.getMessageIdentifier())
                            .build())
                    .emoji(messageRequest.getTextMessage())
                    .build());
        }

        if (chatMessagesSessions != null) {
            for (WebSocketSession s : chatMessagesSessions) {
                try {
                    s.sendMessage(createReactionMessage(s, chatMessage));
                } catch (Exception e) {
                    s.close();
                }
            }
        }
    }

    private TextMessage createReactionMessage(WebSocketSession session, ChatMessages chatMessage) throws Exception {
        String chatMessagesSessionUserIdentifier = iHandlersCommons.getSessionUserIdentifier(session);
        ChatsMessageResponse sessionUserChatResponse = new ChatsMessageResponse(chatMessage, iFeedClient, iAccountsClient, chatMessagesSessionUserIdentifier, "REACTION_MESSAGE", iChatMessagesReactionsRepository);
        String sessionUserResponse = objectMapper.writeValueAsString(sessionUserChatResponse);
        return new TextMessage(sessionUserResponse);
    }
}
