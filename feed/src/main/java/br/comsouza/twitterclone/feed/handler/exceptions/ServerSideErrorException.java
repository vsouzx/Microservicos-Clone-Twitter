package br.comsouza.twitterclone.feed.handler.exceptions;


import br.comsouza.twitterclone.feed.enums.InternalTypeErrorCodesEnum;

public class ServerSideErrorException extends ServerErrorException {

    public ServerSideErrorException() {
        super(InternalTypeErrorCodesEnum.E500000);
    }

    public ServerSideErrorException(String message) {
        super(InternalTypeErrorCodesEnum.E500000, message);
    }
}
