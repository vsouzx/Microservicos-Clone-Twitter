package br.com.souza.twitterclone.notifications.handler.exceptions;

import br.com.souza.twitterclone.notifications.enums.InternalTypeErrorCodesEnum;

public class NotificationTypeNotFoundException extends ErrorCodeException{

    public NotificationTypeNotFoundException() {
        super(InternalTypeErrorCodesEnum.E430001);
    }

    public NotificationTypeNotFoundException(String message) {
        super(InternalTypeErrorCodesEnum.E430001, message);
    }
}
