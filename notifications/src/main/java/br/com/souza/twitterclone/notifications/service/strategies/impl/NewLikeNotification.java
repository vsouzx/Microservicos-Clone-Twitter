package br.com.souza.twitterclone.notifications.service.strategies.impl;

import br.com.souza.twitterclone.notifications.database.model.NotificationsTypes;
import br.com.souza.twitterclone.notifications.service.strategies.INotificationCreationStrategy;
import org.springframework.stereotype.Service;

@Service
public class NewLikeNotification implements INotificationCreationStrategy {

    @Override
    public void createNotification(String tweetIdentifier, NotificationsTypes notificationsTypes) {

    }

}
