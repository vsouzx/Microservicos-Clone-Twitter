package br.comsouza.twitterclone.feed.handler.exceptions;

import br.comsouza.twitterclone.feed.enums.InternalTypeErrorCodesEnum;
public class ApiAuthorizationException extends ErrorCodeException{


    public ApiAuthorizationException(){
        super(InternalTypeErrorCodesEnum.E410016);
    }

    public ApiAuthorizationException(String message){
        super(InternalTypeErrorCodesEnum.E410016, message);
    }
}