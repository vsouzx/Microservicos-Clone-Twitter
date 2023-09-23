package br.comsouza.twitterclone.feed.service.client;

public interface INotificationsClientService {
    
    void createNewNotification(String userSender, String userReceiver, String tweetType, String tweetIdentifier, String authorization);
    
}
