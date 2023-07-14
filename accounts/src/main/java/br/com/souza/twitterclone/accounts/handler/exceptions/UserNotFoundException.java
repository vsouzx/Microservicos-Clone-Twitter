package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class UserNotFoundException extends ErrorCodeException{


    public UserNotFoundException(){
        super(InternalTypeErrorCodesEnum.E410001);
    }

    public UserNotFoundException(String message){
        super(InternalTypeErrorCodesEnum.E410001, message);
    }
}
