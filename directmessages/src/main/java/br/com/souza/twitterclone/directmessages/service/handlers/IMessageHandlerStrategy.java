package br.com.souza.twitterclone.directmessages.service.handlers;

import br.com.souza.twitterclone.directmessages.dto.MessageRequest;

public interface IMessageHandlerStrategy {

    void processMessage(MessageRequest messageRequest, String chatIdentifier, String sessionUserIdentifier, String receiverIdentifier, String sessionToken) throws Exception;

}
