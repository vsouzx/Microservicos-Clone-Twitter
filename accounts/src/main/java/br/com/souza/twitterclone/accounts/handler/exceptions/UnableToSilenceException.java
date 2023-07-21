package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class UnableToSilenceException extends ErrorCodeException{


    public UnableToSilenceException(){
        super(InternalTypeErrorCodesEnum.E410012);
    }

    public UnableToSilenceException(String message){
        super(InternalTypeErrorCodesEnum.E410012, message);
    }
}
