package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class EmailAlreadyConfirmedException extends ErrorCodeException{


    public EmailAlreadyConfirmedException(){
        super(InternalTypeErrorCodesEnum.E410013);
    }

    public EmailAlreadyConfirmedException(String message){
        super(InternalTypeErrorCodesEnum.E410013, message);
    }
}
