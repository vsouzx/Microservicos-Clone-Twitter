package br.com.souza.twitterclone.directmessages.service.handlers.factory;

import br.com.souza.twitterclone.directmessages.service.handlers.IMessageHandlerStrategy;
import br.com.souza.twitterclone.directmessages.service.handlers.impl.HideMessageHandler;
import br.com.souza.twitterclone.directmessages.service.handlers.impl.NewMessageHandler;
import br.com.souza.twitterclone.directmessages.service.handlers.impl.ReactMessageHandler;
import org.springframework.stereotype.Service;

@Service
public class MessageHandlerFactory {

    private final NewMessageHandler newMessageHandler;
    private final HideMessageHandler hideMessageHandler;
    private final ReactMessageHandler reactMessageHandler;

    public MessageHandlerFactory(NewMessageHandler newMessageHandler,
                                 HideMessageHandler hideMessageHandler,
                                 ReactMessageHandler reactMessageHandler) {
        this.newMessageHandler = newMessageHandler;
        this.hideMessageHandler = hideMessageHandler;
        this.reactMessageHandler = reactMessageHandler;
    }

    public IMessageHandlerStrategy getStrategy(String type) throws Exception {
        return switch (type.toUpperCase()) {
            case "NEW_MESSAGE" -> newMessageHandler;
            case "HIDE_MESSAGE" -> hideMessageHandler;
            case "REACTION_MESSAGE" -> reactMessageHandler;
            default -> throw new Exception("Type not supported: " + type);
        };
    }
}
