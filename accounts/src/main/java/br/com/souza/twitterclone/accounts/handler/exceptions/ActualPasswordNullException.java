package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class ActualPasswordNullException extends ErrorCodeException{


    public ActualPasswordNullException(){
        super(InternalTypeErrorCodesEnum.E410003);
    }

    public ActualPasswordNullException(String message){
        super(InternalTypeErrorCodesEnum.E410003, message);
    }
}
