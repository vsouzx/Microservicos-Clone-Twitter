package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class BlockedUserException extends ErrorCodeException{


    public BlockedUserException(){
        super(InternalTypeErrorCodesEnum.E410009);
    }

    public BlockedUserException(String message){
        super(InternalTypeErrorCodesEnum.E410009, message);
    }
}
