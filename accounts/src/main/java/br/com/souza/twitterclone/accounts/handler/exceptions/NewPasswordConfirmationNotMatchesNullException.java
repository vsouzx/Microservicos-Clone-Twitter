package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class NewPasswordConfirmationNotMatchesNullException extends ErrorCodeException{


    public NewPasswordConfirmationNotMatchesNullException(){
        super(InternalTypeErrorCodesEnum.E410006);
    }

    public NewPasswordConfirmationNotMatchesNullException(String message){
        super(InternalTypeErrorCodesEnum.E410006, message);
    }
}
