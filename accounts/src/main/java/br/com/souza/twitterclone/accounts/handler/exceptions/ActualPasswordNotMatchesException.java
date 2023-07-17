package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class ActualPasswordNotMatchesException extends ErrorCodeException{


    public ActualPasswordNotMatchesException(){
        super(InternalTypeErrorCodesEnum.E410004);
    }

    public ActualPasswordNotMatchesException(String message){
        super(InternalTypeErrorCodesEnum.E410004, message);
    }
}
