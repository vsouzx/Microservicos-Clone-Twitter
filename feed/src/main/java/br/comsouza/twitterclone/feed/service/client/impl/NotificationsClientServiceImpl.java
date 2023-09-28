package br.comsouza.twitterclone.feed.service.client.impl;

import br.comsouza.twitterclone.feed.client.IAccountsClient;
import br.comsouza.twitterclone.feed.client.INotificationClient;
import br.comsouza.twitterclone.feed.dto.client.DeleteNotificationRequest;
import br.comsouza.twitterclone.feed.dto.client.NewNotificationRequest;
import br.comsouza.twitterclone.feed.service.client.INotificationsClientService;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationsClientServiceImpl implements INotificationsClientService {

    private final INotificationClient iNotificationsClient;
    private final IAccountsClient iAccountsClient;

    public NotificationsClientServiceImpl(INotificationClient iNotificationsClient,
                                          IAccountsClient iAccountsClient) {
        this.iNotificationsClient = iNotificationsClient;
        this.iAccountsClient = iAccountsClient;
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

    @Override
    public void deleteNotification(DeleteNotificationRequest request, String authorization) {
            iNotificationsClient.deleteNotification(request, authorization);
    }

    @Override
    public void notifyAlerters(String userSender, String tweetType, String tweetIdentifier, String authorization) {

        new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                List<String> receivers = iAccountsClient.getAlertedUsers(authorization);
                try {
                    receivers.forEach(userReceiver -> {
                        iNotificationsClient.createNewNotification(NewNotificationRequest.builder()
                                .tweetIdentifier(tweetIdentifier)
                                .userSenderIdentifier(userSender)
                                .userReceiverIdentifier(userReceiver)
                                .typeDescription(tweetType)
                                .build(), authorization);
                    });
                } catch (Exception e) {
                    log.error("Erro ao criar notificação: ", e);
                }
            }
        }.start();
    }
}