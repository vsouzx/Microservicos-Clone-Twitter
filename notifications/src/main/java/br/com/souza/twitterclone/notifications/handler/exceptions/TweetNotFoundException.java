package br.com.souza.twitterclone.notifications.handler.exceptions;

import br.com.souza.twitterclone.notifications.enums.InternalTypeErrorCodesEnum;

public class TweetNotFoundException extends ErrorCodeException{

    public TweetNotFoundException() {
        super(InternalTypeErrorCodesEnum.E420001);
    }

    public TweetNotFoundException(String message) {
        super(InternalTypeErrorCodesEnum.E420001, message);
    }
}
