package br.comsouza.twitterclone.feed.handler.exceptions;


import br.comsouza.twitterclone.feed.enums.InternalTypeErrorCodesEnum;

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
