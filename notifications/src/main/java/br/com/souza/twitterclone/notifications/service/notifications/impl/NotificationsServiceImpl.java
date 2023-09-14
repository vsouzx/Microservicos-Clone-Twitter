package br.com.souza.twitterclone.notifications.service.notifications.impl;

import br.com.souza.twitterclone.notifications.database.model.NotificationsTypes;
import br.com.souza.twitterclone.notifications.database.repository.INotificationsTypesRepository;
import br.com.souza.twitterclone.notifications.dto.notifications.NewNotificationRequest;
import br.com.souza.twitterclone.notifications.service.factories.NotificationFactory;
import br.com.souza.twitterclone.notifications.service.notifications.INotificationsService;
import br.com.souza.twitterclone.notifications.service.strategies.INotificationCreationStrategy;
import org.springframework.stereotype.Service;

@Service
public class NotificationsServiceImpl implements INotificationsService {

    private final INotificationsTypesRepository iNotificationsTypesRepository;
    private final NotificationFactory notificationFactory;

    public NotificationsServiceImpl(INotificationsTypesRepository iNotificationsTypesRepository,
                                    NotificationFactory notificationFactory) {
        this.iNotificationsTypesRepository = iNotificationsTypesRepository;
        this.notificationFactory = notificationFactory;
    }

    @Override
    public void createNewNotification(NewNotificationRequest request) throws Exception {
        NotificationsTypes notificationType = iNotificationsTypesRepository.findByDescription(request.getTypeDescription())
                .orElseThrow(() -> new Exception("Notification type not found"));

        INotificationCreationStrategy strategy = notificationFactory.getStrategy(request.getTypeDescription());
        strategy.createNotification(request.getTweetIdentifier(), notificationType);

        //TODO: Após criar, notificar via SSE
    }
}
