package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class InvalidUsernameRegexException extends ErrorCodeException{


    public InvalidUsernameRegexException(){
        super(InternalTypeErrorCodesEnum.E410007);
    }

    public InvalidUsernameRegexException(String message){
        super(InternalTypeErrorCodesEnum.E410007, message);
    }
}
