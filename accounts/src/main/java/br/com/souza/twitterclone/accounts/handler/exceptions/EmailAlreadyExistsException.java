package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class EmailAlreadyExistsException extends ErrorCodeException{


    public EmailAlreadyExistsException(){
        super(InternalTypeErrorCodesEnum.E410002);
    }

    public EmailAlreadyExistsException(String message){
        super(InternalTypeErrorCodesEnum.E410002, message);
    }
}
