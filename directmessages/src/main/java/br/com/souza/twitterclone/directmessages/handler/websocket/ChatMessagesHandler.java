package br.com.souza.twitterclone.directmessages.handler.websocket;

import br.com.souza.twitterclone.directmessages.configuration.authorization.TokenProvider;
import br.com.souza.twitterclone.directmessages.database.model.DmChats;
import br.com.souza.twitterclone.directmessages.database.repository.IDmChatsRepository;
import br.com.souza.twitterclone.directmessages.dto.MessageRequest;
import br.com.souza.twitterclone.directmessages.service.handlers.IMessageHandlerStrategy;
import br.com.souza.twitterclone.directmessages.service.handlers.factory.MessageHandlerFactory;
import br.com.souza.twitterclone.directmessages.util.SingletonChatMessagesConnections;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
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
    private final TokenProvider tokenProvider;
    private final IDmChatsRepository iDmChatsRepository;
    private final ObjectMapper mapper;
    private final MessageHandlerFactory messageHandlerFactory;

    public ChatMessagesHandler(TokenProvider tokenProvider,
                               IDmChatsRepository iDmChatsRepository,
                               ObjectMapper mapper,
                               MessageHandlerFactory messageHandlerFactory) {
        this.iDmChatsRepository = iDmChatsRepository;
        this.mapper = mapper;
        this.messageHandlerFactory = messageHandlerFactory;
        this.singletonChatMessagesConnections = SingletonChatMessagesConnections.getInstance();
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

                MessageRequest messageRequest = mapper.readValue(message.getPayload(), MessageRequest.class);

                IMessageHandlerStrategy strategy = messageHandlerFactory.getStrategy(messageRequest.getType());
                strategy.processMessage(messageRequest, chatIdentifier.get(), userIdentifier, receiverIdentifier, sessionToken.get());

            } else {
                session.close(CloseStatus.BAD_DATA);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Optional<String> chatIdentifier = getChatIdentifier(session);
        if (chatIdentifier.isPresent()) {
            singletonChatMessagesConnections.remove(chatIdentifier.get(), session);
        } else {
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        session.close(CloseStatus.SERVER_ERROR);
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

    private String getReceiverIdentifier(DmChats chat, String sessionUserIdentifier) {
        return chat.getUserIdentifier1().equals(sessionUserIdentifier) ? chat.getUserIdentifier2() : chat.getUserIdentifier1();
    }
}