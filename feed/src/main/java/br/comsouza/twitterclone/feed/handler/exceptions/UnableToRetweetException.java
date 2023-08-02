package br.comsouza.twitterclone.feed.handler.exceptions;

import br.comsouza.twitterclone.feed.enums.InternalTypeErrorCodesEnum;

public class UnableToRetweetException extends ErrorCodeException{

    public UnableToRetweetException() {
        super(InternalTypeErrorCodesEnum.E420002);
    }

    public UnableToRetweetException(String message) {
        super(InternalTypeErrorCodesEnum.E420002, message);
    }
}
