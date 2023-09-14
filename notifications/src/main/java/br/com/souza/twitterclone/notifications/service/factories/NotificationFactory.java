package br.com.souza.twitterclone.notifications.service.factories;

import br.com.souza.twitterclone.notifications.service.strategies.INotificationCreationStrategy;
import br.com.souza.twitterclone.notifications.service.strategies.impl.NewCommentNotification;
import br.com.souza.twitterclone.notifications.service.strategies.impl.NewFollowerNotification;
import br.com.souza.twitterclone.notifications.service.strategies.impl.NewLikeNotification;
import org.springframework.stereotype.Service;

@Service
public class NotificationFactory {

    private final NewCommentNotification newCommentNotification;
    private final NewFollowerNotification newFollowerNotification;
    private final NewLikeNotification newLikeNotification;

    public NotificationFactory(NewCommentNotification newCommentNotification,
                               NewFollowerNotification newFollowerNotification,
                               NewLikeNotification newLikeNotification) {
        this.newCommentNotification = newCommentNotification;
        this.newFollowerNotification = newFollowerNotification;
        this.newLikeNotification = newLikeNotification;
    }

    public INotificationCreationStrategy getStrategy(String typeDescription) throws Exception {
        return switch (typeDescription.toUpperCase()) {
            case "NEW_FOLLOWER" -> newFollowerNotification;
            case "NEW_COMMENT" -> newCommentNotification;
            case "NEW_LIKE" -> newLikeNotification;
            default -> throw new Exception("Notification type does not exists: " + typeDescription);
        };
    }
}
