package br.comsouza.twitterclone.feed.handler.exceptions;

import br.comsouza.twitterclone.feed.enums.InternalTypeErrorCodesEnum;

public class InvalidTweetException extends ErrorCodeException{

    public InvalidTweetException() {
        super(InternalTypeErrorCodesEnum.E420000);
    }

    public InvalidTweetException(String message) {
        super(InternalTypeErrorCodesEnum.E420000, message);
    }
}
