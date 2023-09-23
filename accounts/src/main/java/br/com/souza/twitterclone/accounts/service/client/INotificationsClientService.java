package br.com.souza.twitterclone.accounts.service.client;

public interface INotificationsClientService {

    void createNewNotification(String userSender, String userReceiver, String tweetType, String tweetIdentifier, String authorization);

}
