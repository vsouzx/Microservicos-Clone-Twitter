package br.comsouza.twitterclone.feed.service.client.impl;

import br.comsouza.twitterclone.feed.client.INotificationClient;
import br.comsouza.twitterclone.feed.dto.client.NewNotificationRequest;
import br.comsouza.twitterclone.feed.service.client.INotificationsClientService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationsClientServiceImpl implements INotificationsClientService {

    private final INotificationClient iNotificationsClient;

    public NotificationsClientServiceImpl(INotificationClient iNotificationsClient) {
        this.iNotificationsClient = iNotificationsClient;
    }

    @Override
    public void createNewNotification(String userSender, String userReceiver, String tweetType, String tweetIdentifier, String authorization) {
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
                            .build(), authorization);
                } catch (Exception e) {
                    log.error("Erro ao criar notificação: ", e);
                }
            }
        }.start();
    }
}