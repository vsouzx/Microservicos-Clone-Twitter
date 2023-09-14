package br.com.souza.twitterclone.notifications.service.strategies.impl;

import br.com.souza.twitterclone.notifications.database.model.Notifications;
import br.com.souza.twitterclone.notifications.database.model.NotificationsTypes;
import br.com.souza.twitterclone.notifications.database.repository.INotificationsRepository;
import br.com.souza.twitterclone.notifications.service.strategies.INotificationCreationStrategy;
import br.com.souza.twitterclone.notifications.util.UsefulDate;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class NewFollowerNotification implements INotificationCreationStrategy {

    private final INotificationsRepository iNotificationsRepository;

    public NewFollowerNotification(INotificationsRepository iNotificationsRepository) {
        this.iNotificationsRepository = iNotificationsRepository;
    }

    @Override
    public void createNotification(String tweetIdentifier, NotificationsTypes notificationsTypes) {
        iNotificationsRepository.save(Notifications.builder()
                        .identifier(UUID.randomUUID().toString())
                        .tweetIdentifier(tweetIdentifier)
                        .typeIdentifier(notificationsTypes.getTypeIdentifier())
                        .visualized(false)
                        .creationDate(UsefulDate.now())
                .build());
    }

}
