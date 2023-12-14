package br.com.souza.twitterclone.accounts.service.client.impl;

import br.com.souza.twitterclone.accounts.client.INotificationsClient;
import br.com.souza.twitterclone.accounts.dto.client.DeleteNotificationRequest;
import br.com.souza.twitterclone.accounts.dto.client.NewNotificationRequest;
import br.com.souza.twitterclone.accounts.service.client.INotificationsClientService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationsClientServiceImpl implements INotificationsClientService {

    private final INotificationsClient iNotificationsClient;

    public NotificationsClientServiceImpl(INotificationsClient iNotificationsClient) {
        this.iNotificationsClient = iNotificationsClient;
    }

    @Override
    public void createNewNotification(String userSender, String userReceiver, String tweetType, String tweetIdentifier) {
        new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                try {
                    iNotificationsClient.createNewNotification(NewNotificationRequest.builder()
                            .tweetIdentifier(tweetIdentifier)
                            .userSenderIdentifier(userSender)
                            .userReceiverIdentifier(userReceiver)
                            .typeDescription(tweetType)
                            .build());
                } catch (Exception e) {
                    log.error("Erro ao criar notificação: ", e);
                }
            }
        }.start();
    }

    @Override
    public void deleteNotification(DeleteNotificationRequest request) {
        iNotificationsClient.deleteNotification(request);
    }
}
