package br.com.souza.twitterclone.authentication.handler.exceptions;

import br.com.souza.twitterclone.authentication.enums.InternalTypeErrorCodesEnum;

public class NotConfirmedEmailException extends ErrorCodeException{

    public NotConfirmedEmailException() {
        super(InternalTypeErrorCodesEnum.E400002);
    }

    public NotConfirmedEmailException(String message) {
        super(InternalTypeErrorCodesEnum.E400002, message);
    }
}