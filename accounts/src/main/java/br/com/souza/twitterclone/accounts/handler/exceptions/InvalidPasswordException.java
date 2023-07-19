package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class InvalidPasswordException extends ErrorCodeException{


    public InvalidPasswordException(){
        super(InternalTypeErrorCodesEnum.E410008);
    }

    public InvalidPasswordException(String message){
        super(InternalTypeErrorCodesEnum.E410008, message);
    }
}
