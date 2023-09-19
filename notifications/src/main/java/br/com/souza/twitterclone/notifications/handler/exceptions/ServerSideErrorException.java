package br.com.souza.twitterclone.notifications.handler.exceptions;


import br.com.souza.twitterclone.notifications.enums.InternalTypeErrorCodesEnum;

public class ServerSideErrorException extends ServerErrorException{

    public ServerSideErrorException() {
        super(InternalTypeErrorCodesEnum.E500000);
    }

    public ServerSideErrorException(String message) {
        super(InternalTypeErrorCodesEnum.E500000, message);
    }
}
