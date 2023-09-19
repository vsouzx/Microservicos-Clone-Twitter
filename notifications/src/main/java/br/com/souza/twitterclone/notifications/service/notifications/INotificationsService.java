package br.com.souza.twitterclone.notifications.service.notifications;

import br.com.souza.twitterclone.notifications.dto.notifications.NewNotificationRequest;

public interface INotificationsService {

    void createNewNotification(NewNotificationRequest request, String authorization) throws Exception;
}
