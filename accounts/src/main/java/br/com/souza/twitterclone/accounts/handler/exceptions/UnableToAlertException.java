package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class UnableToAlertException extends ErrorCodeException{


    public UnableToAlertException(){
        super(InternalTypeErrorCodesEnum.E410015);
    }

    public UnableToAlertException(String message){
        super(InternalTypeErrorCodesEnum.E410015, message);
    }
}
