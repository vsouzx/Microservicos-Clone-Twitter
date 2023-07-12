package br.com.souza.twitterclone.authentication.handler.exceptions;

import br.com.souza.twitterclone.authentication.enums.InternalTypeErrorCodesEnum;
public class BadCredencialsException extends ErrorCodeException{

    public BadCredencialsException() {
        super(InternalTypeErrorCodesEnum.E400001);
    }

    public BadCredencialsException(String message) {
        super(InternalTypeErrorCodesEnum.E400001, message);
    }
}