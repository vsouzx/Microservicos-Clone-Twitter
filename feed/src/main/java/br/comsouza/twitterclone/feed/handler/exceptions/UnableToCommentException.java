package br.comsouza.twitterclone.feed.handler.exceptions;

import br.comsouza.twitterclone.feed.enums.InternalTypeErrorCodesEnum;

public class UnableToCommentException extends ErrorCodeException{

    public UnableToCommentException() {
        super(InternalTypeErrorCodesEnum.E420003);
    }

    public UnableToCommentException(String message) {
        super(InternalTypeErrorCodesEnum.E420003, message);
    }
}
