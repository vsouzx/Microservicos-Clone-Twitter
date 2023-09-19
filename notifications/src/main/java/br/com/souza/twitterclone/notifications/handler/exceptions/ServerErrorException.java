package br.com.souza.twitterclone.notifications.handler.exceptions;

import br.com.souza.twitterclone.notifications.enums.InternalTypeErrorCodesEnum;

public class ServerErrorException extends Exception{

    protected InternalTypeErrorCodesEnum errorcode;

    protected ServerErrorException(InternalTypeErrorCodesEnum errorcode) {
        super(errorcode.getMessage());
        this.errorcode = errorcode;
    }

    protected ServerErrorException(InternalTypeErrorCodesEnum errorcode, Object... args) {
        super(String.format(errorcode.getMessage(), args));
        this.errorcode = errorcode;
    }

    public InternalTypeErrorCodesEnum getErrorcode() {
        return errorcode;
    }
}
