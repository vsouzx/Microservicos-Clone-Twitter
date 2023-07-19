package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class UnableToFollowException extends ErrorCodeException{


    public UnableToFollowException(){
        super(InternalTypeErrorCodesEnum.E410010);
    }

    public UnableToFollowException(String message){
        super(InternalTypeErrorCodesEnum.E410010, message);
    }
}
