package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class UsernameAlreadyExistsException extends ErrorCodeException{


    public UsernameAlreadyExistsException(){
        super(InternalTypeErrorCodesEnum.E410001);
    }

    public UsernameAlreadyExistsException(String message){
        super(InternalTypeErrorCodesEnum.E410001, message);
    }
}
