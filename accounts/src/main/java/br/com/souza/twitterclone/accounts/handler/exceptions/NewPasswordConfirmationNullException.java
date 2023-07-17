package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class NewPasswordConfirmationNullException extends ErrorCodeException{


    public NewPasswordConfirmationNullException(){
        super(InternalTypeErrorCodesEnum.E410005);
    }

    public NewPasswordConfirmationNullException(String message){
        super(InternalTypeErrorCodesEnum.E410005, message);
    }
}
