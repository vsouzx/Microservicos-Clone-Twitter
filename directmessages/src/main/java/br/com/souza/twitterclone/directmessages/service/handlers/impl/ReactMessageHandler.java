package br.com.souza.twitterclone.directmessages.service.handlers.impl;

import br.com.souza.twitterclone.directmessages.dto.MessageRequest;
import br.com.souza.twitterclone.directmessages.service.handlers.IMessageHandlerStrategy;
import org.springframework.stereotype.Service;

@Service
public class ReactMessageHandler implements IMessageHandlerStrategy {

    @Override
    public void processMessage(MessageRequest messageRequest, String chatIdentifier, String sessionUserIdentifier, String receiverIdentifier, String sessionToken) throws Exception {
        //TODO
    }
}
