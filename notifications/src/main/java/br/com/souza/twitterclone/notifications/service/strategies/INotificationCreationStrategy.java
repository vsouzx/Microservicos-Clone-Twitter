package br.com.souza.twitterclone.notifications.service.strategies;

import br.com.souza.twitterclone.notifications.database.model.NotificationsTypes;

public interface INotificationCreationStrategy {

    void createNotification(String tweetIdentifier, NotificationsTypes notificationType);
}
