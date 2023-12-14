package br.com.souza.twitterclone.directmessages.handler.exceptions;

import br.com.souza.twitterclone.directmessages.enums.InternalTypeErrorCodesEnum;

public class ApiAuthorizationException extends ErrorCodeException{


    public ApiAuthorizationException(){
        super(InternalTypeErrorCodesEnum.E410016);
    }

    public ApiAuthorizationException(String message){
        super(InternalTypeErrorCodesEnum.E410016, message);
    }
}