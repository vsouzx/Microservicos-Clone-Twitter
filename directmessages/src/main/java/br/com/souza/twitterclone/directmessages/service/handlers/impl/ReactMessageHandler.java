package br.com.souza.twitterclone.directmessages.service.handlers.impl;

import br.com.souza.twitterclone.directmessages.client.IAccountsClient;
import br.com.souza.twitterclone.directmessages.client.IFeedClient;
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
import java.util.Set;
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

    public ReactMessageHandler(IChatMessagesRepository iChatMessagesRepository,
                               IAccountsClient iAccountsClient,
                               IFeedClient iFeedClient,
                               ObjectMapper objectMapper,
                               IHandlersCommons iHandlersCommons) {
        this.iChatMessagesRepository = iChatMessagesRepository;
        this.singletonChatMessagesConnections = SingletonChatMessagesConnections.getInstance();
        this.iAccountsClient = iAccountsClient;
        this.iFeedClient = iFeedClient;
        this.objectMapper = objectMapper;
        this.iHandlersCommons = iHandlersCommons;
    }

    @Override
    public void processMessage(MessageRequest messageRequest, String chatIdentifier, String sessionUserIdentifier, String receiverIdentifier, String sessionToken) throws Exception {
        Set<WebSocketSession> chatMessagesSessions = singletonChatMessagesConnections.get(chatIdentifier);

        ChatMessages chatMessage = iChatMessagesRepository.findById(messageRequest.getMessageIdentifier())
                .orElseThrow(() -> new Exception("Message not found"));

        if(chatMessage.getEmoji() != null){
            chatMessage.setEmoji(null);
        }else{
            chatMessage.setEmoji(messageRequest.getTextMessage());
        }

        iChatMessagesRepository.save(chatMessage);

        if (chatMessagesSessions != null) {
            for (WebSocketSession s : chatMessagesSessions) {
                try {
                    s.sendMessage(createReactionMessage(s, sessionToken, chatMessage));
                } catch (Exception e) {
                    s.close();
                }
            }
        }
    }

    private TextMessage createReactionMessage(WebSocketSession session, String sessionToken, ChatMessages chatMessage) throws Exception {
        String chatMessagesSessionUserIdentifier = iHandlersCommons.getSessionUserIdentifier(session);
        ChatsMessageResponse sessionUserChatResponse = new ChatsMessageResponse(chatMessage, iFeedClient, iAccountsClient, sessionToken, chatMessagesSessionUserIdentifier, "REACTION_MESSAGE");
        String sessionUserResponse = objectMapper.writeValueAsString(sessionUserChatResponse);
        return new TextMessage(sessionUserResponse);
    }
}
