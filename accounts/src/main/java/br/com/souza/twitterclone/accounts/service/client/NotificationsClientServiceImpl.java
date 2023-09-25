package br.com.souza.twitterclone.accounts.service.client;

import br.com.souza.twitterclone.accounts.client.INotificationsClient;
import br.com.souza.twitterclone.accounts.dto.client.NewNotificationRequest;
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
