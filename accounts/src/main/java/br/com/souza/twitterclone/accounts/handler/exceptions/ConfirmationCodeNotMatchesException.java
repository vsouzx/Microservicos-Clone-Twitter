package br.com.souza.twitterclone.accounts.handler.exceptions;

import br.com.souza.twitterclone.accounts.enums.InternalTypeErrorCodesEnum;

public class ConfirmationCodeNotMatchesException extends ErrorCodeException{


    public ConfirmationCodeNotMatchesException(){
        super(InternalTypeErrorCodesEnum.E410014);
    }

    public ConfirmationCodeNotMatchesException(String message){
        super(InternalTypeErrorCodesEnum.E410014, message);
    }
}
