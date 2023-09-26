package br.comsouza.twitterclone.feed.service.client;

import br.comsouza.twitterclone.feed.dto.client.DeleteNotificationRequest;

public interface INotificationsClientService {
    
    void createNewNotification(String userSender, String userReceiver, String tweetType, String tweetIdentifier, String authorization);

    void deleteNotification(DeleteNotificationRequest request, String authorization);
}
