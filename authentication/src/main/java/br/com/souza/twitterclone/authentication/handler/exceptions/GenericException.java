package br.com.souza.twitterclone.authentication.handler.exceptions;

import br.com.souza.twitterclone.authentication.enums.InternalTypeErrorCodesEnum;

public class GenericException extends ErrorCodeException{

    public GenericException() {
        super(InternalTypeErrorCodesEnum.E500000);
    }

    public GenericException(String message) {
        super(InternalTypeErrorCodesEnum.E500000, message);
    }
}