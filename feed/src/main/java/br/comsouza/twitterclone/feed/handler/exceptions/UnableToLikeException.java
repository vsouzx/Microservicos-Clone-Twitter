package br.comsouza.twitterclone.feed.handler.exceptions;

import br.comsouza.twitterclone.feed.enums.InternalTypeErrorCodesEnum;

public class UnableToLikeException extends ErrorCodeException{

    public UnableToLikeException() {
        super(InternalTypeErrorCodesEnum.E420004);
    }

    public UnableToLikeException(String message) {
        super(InternalTypeErrorCodesEnum.E420004, message);
    }
}
