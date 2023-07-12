package br.com.souza.twitterclone.authentication.handler.exceptions;

import br.com.souza.twitterclone.authentication.enums.InternalTypeErrorCodesEnum;

public class ErrorCodeException extends Exception{

    protected InternalTypeErrorCodesEnum errorcode;

    protected ErrorCodeException(InternalTypeErrorCodesEnum errorcode) {
        super(errorcode.getMessage());
        this.errorcode = errorcode;
    }

    protected ErrorCodeException(InternalTypeErrorCodesEnum errorcode, Object... args) {
        super(String.format(errorcode.getMessage(), args));
        this.errorcode = errorcode;
    }

    public InternalTypeErrorCodesEnum getErrorcode() {
        return errorcode;
    }
}
