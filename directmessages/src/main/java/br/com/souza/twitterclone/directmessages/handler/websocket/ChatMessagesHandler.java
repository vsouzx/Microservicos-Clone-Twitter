package br.com.souza.twitterclone.directmessages.handler.websocket;

import br.com.souza.twitterclone.directmessages.database.model.DmChats;
import br.com.souza.twitterclone.directmessages.database.repository.IDmChatsRepository;
import br.com.souza.twitterclone.directmessages.dto.MessageRequest;
import br.com.souza.twitterclone.directmessages.service.commons.IHandlersCommons;
import br.com.souza.twitterclone.directmessages.service.handlers.IMessageHandlerStrategy;
import br.com.souza.twitterclone.directmessages.service.handlers.factory.MessageHandlerFactory;
import br.com.souza.twitterclone.directmessages.service.redis.RedisService;
import br.com.souza.twitterclone.directmessages.util.SingletonChatMessagesConnections;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChatMessagesHandler extends TextWebSocketHandler {

    private final SingletonChatMessagesConnections singletonChatMessagesConnections;
    private final IDmChatsRepository iDmChatsRepository;
    private final ObjectMapper mapper;
    private final MessageHandlerFactory messageHandlerFactory;
    private final IHandlersCommons iHandlersCommons;
    private final RedisService redisService;

    public ChatMessagesHandler(IDmChatsRepository iDmChatsRepository,
                               ObjectMapper mapper,
                               MessageHandlerFactory messageHandlerFactory,
                               IHandlersCommons iHandlersCommons,
                               RedisService redisService) {
        this.iDmChatsRepository = iDmChatsRepository;
        this.mapper = mapper;
        this.messageHandlerFactory = messageHandlerFactory;
        this.iHandlersCommons = iHandlersCommons;
        this.redisService = redisService;
        this.singletonChatMessagesConnections = SingletonChatMessagesConnections.getInstance();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Optional<String> sessionToken = iHandlersCommons.sessionToken(session);
        if (sessionToken.isPresent()) {
            try{
                redisService.isValidUser(sessionToken.get());
            }catch (Exception e){
                session.close(CloseStatus.POLICY_VIOLATION);
            }

            Optional<String> chatIdentifier = iHandlersCommons.getChatIdentifier(session);
            if (chatIdentifier.isPresent()) {
                singletonChatMessagesConnections.put(chatIdentifier.get(), session);
                session.sendMessage(new TextMessage("pong"));
            } else {
                session.close(CloseStatus.BAD_DATA);
            }
        } else {
            session.close(CloseStatus.POLICY_VIOLATION);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Optional<String> sessionToken = iHandlersCommons.sessionToken(session);
        if (sessionToken.isPresent()) {
            try{
                redisService.isValidUser(sessionToken.get());
            }catch (Exception e){
                session.close(CloseStatus.POLICY_VIOLATION);
            }

            Optional<String> chatIdentifier = iHandlersCommons.getChatIdentifier(session);
            if (chatIdentifier.isPresent()) {
                Optional<DmChats> chat = iDmChatsRepository.findById(chatIdentifier.get());
                if (chat.isEmpty()) {
                    session.close(CloseStatus.BAD_DATA);
                    return;
                }
                if(message.getPayload().equals("ping")){
                    session.sendMessage(new TextMessage("pong"));
                    return;
                }
                String receiverIdentifier = iHandlersCommons.getReceiverIdentifier(chat.get(), sessionToken.get());
                MessageRequest messageRequest = mapper.readValue(message.getPayload(), MessageRequest.class);
                IMessageHandlerStrategy strategy = messageHandlerFactory.getStrategy(messageRequest.getType());
                strategy.processMessage(messageRequest, chatIdentifier.get(), sessionToken.get(), receiverIdentifier);
            } else {
                session.close(CloseStatus.BAD_DATA);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Optional<String> chatIdentifier = iHandlersCommons.getChatIdentifier(session);
        if (chatIdentifier.isPresent()) {
            singletonChatMessagesConnections.remove(chatIdentifier.get(), session);
        } else {
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        session.close(CloseStatus.SERVER_ERROR);
    }
}