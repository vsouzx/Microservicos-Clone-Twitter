package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class NonexistentPendingFollowException extends ErrorCodeException{


    public NonexistentPendingFollowException(){
        super(InternalTypeErrorCodesEnum.E410011);
    }

    public NonexistentPendingFollowException(String message){
        super(InternalTypeErrorCodesEnum.E410011, message);
    }
}
