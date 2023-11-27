package br.comsouza.twitterclone.feed.handler.exceptions;

import br.comsouza.twitterclone.feed.enums.InternalTypeErrorCodesEnum;

public class InvalidAttachmentException extends ErrorCodeException{

    public InvalidAttachmentException() {
        super(InternalTypeErrorCodesEnum.E420005);
    }

    public InvalidAttachmentException(String message) {
        super(InternalTypeErrorCodesEnum.E420005, message);
    }
}
