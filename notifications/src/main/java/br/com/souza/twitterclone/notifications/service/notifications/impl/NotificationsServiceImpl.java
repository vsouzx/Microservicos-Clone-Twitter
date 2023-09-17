package br.com.souza.twitterclone.notifications.service.notifications.impl;

import br.com.souza.twitterclone.notifications.database.model.Notifications;
import br.com.souza.twitterclone.notifications.database.model.NotificationsTypes;
import br.com.souza.twitterclone.notifications.database.repository.INotificationsRepository;
import br.com.souza.twitterclone.notifications.database.repository.INotificationsTypesRepository;
import br.com.souza.twitterclone.notifications.dto.notifications.NewNotificationRequest;
import br.com.souza.twitterclone.notifications.service.notifications.INotificationsService;
import br.com.souza.twitterclone.notifications.util.UsefulDate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationsServiceImpl implements INotificationsService {

    private final INotificationsTypesRepository iNotificationsTypesRepository;
    private final INotificationsRepository iNotificationsRepository;

    public NotificationsServiceImpl(INotificationsTypesRepository iNotificationsTypesRepository,
                                    INotificationsRepository iNotificationsRepository) {
        this.iNotificationsTypesRepository = iNotificationsTypesRepository;
        this.iNotificationsRepository = iNotificationsRepository;
    }

    @Override
    public void createNewNotification(NewNotificationRequest request) throws Exception {
        NotificationsTypes notificationType = iNotificationsTypesRepository.findByDescription(request.getTypeDescription())
                .orElseThrow(() -> new Exception("Notification type not found"));

        //TODO validar se tweet existe

        iNotificationsRepository.save(Notifications.builder()
                .identifier(UUID.randomUUID().toString())
                .tweetIdentifier(request.getTweetIdentifier())
                .typeIdentifier(notificationType.getTypeIdentifier())
                .visualized(false)
                .creationDate(UsefulDate.now())
                .build());

        //TODO: Após criar, notificar via SSE
    }
}
