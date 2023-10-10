package br.com.souza.twitterclone.directmessages.configuration.websocket;

import br.com.souza.twitterclone.directmessages.handler.websocket.ChatMessagesHandler;
import br.com.souza.twitterclone.directmessages.handler.websocket.DmChatsHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final DmChatsHandler dmChatsHandler;
    private final ChatMessagesHandler chatMessagesHandler;

    public WebSocketConfig(DmChatsHandler dmChatsHandler,
                           ChatMessagesHandler chatMessagesHandler) {
        this.dmChatsHandler = dmChatsHandler;
        this.chatMessagesHandler = chatMessagesHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(dmChatsHandler, "/ws/dm-chats")
                .setAllowedOriginPatterns("*");

        registry.addHandler(chatMessagesHandler, "/ws/chat-messages")
                .setAllowedOriginPatterns("*");

    }
}