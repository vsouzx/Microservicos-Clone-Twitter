package br.comsouza.twitterclone.feed.handler.exceptions;

import br.comsouza.twitterclone.feed.enums.InternalTypeErrorCodesEnum;

public class TweetNotFoundException extends ErrorCodeException{

    public TweetNotFoundException() {
        super(InternalTypeErrorCodesEnum.E420001);
    }

    public TweetNotFoundException(String message) {
        super(InternalTypeErrorCodesEnum.E420001, message);
    }
}
