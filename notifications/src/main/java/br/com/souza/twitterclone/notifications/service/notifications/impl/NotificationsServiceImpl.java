package br.com.souza.twitterclone.notifications.service.notifications.impl;

import br.com.souza.twitterclone.notifications.client.IAccountsClient;
import br.com.souza.twitterclone.notifications.client.IFeedClient;
import br.com.souza.twitterclone.notifications.database.model.Notifications;
import br.com.souza.twitterclone.notifications.database.model.NotificationsTypes;
import br.com.souza.twitterclone.notifications.database.repository.INotificationsRepository;
import br.com.souza.twitterclone.notifications.database.repository.INotificationsTypesRepository;
import br.com.souza.twitterclone.notifications.dto.client.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.notifications.dto.notifications.DeleteNotificationRequest;
import br.com.souza.twitterclone.notifications.dto.notifications.NewNotificationRequest;
import br.com.souza.twitterclone.notifications.dto.notifications.NotificationsResponse;
import br.com.souza.twitterclone.notifications.handler.exceptions.NotificationTypeNotFoundException;
import br.com.souza.twitterclone.notifications.handler.exceptions.ServerSideErrorException;
import br.com.souza.twitterclone.notifications.handler.exceptions.TweetNotFoundException;
import br.com.souza.twitterclone.notifications.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.notifications.service.notifications.INotificationsService;
import br.com.souza.twitterclone.notifications.service.notifications.ISseService;
import br.com.souza.twitterclone.notifications.util.UsefulDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class NotificationsServiceImpl implements INotificationsService {

    private final INotificationsTypesRepository iNotificationsTypesRepository;
    private final INotificationsRepository iNotificationsRepository;
    private final IFeedClient iFeedClient;
    private final IAccountsClient iAccountsClient;
    private final ISseService iSseService;

    public NotificationsServiceImpl(INotificationsTypesRepository iNotificationsTypesRepository,
                                    INotificationsRepository iNotificationsRepository,
                                    IFeedClient iFeedClient,
                                    IAccountsClient iAccountsClient,
                                    ISseService iSseService) {
        this.iNotificationsTypesRepository = iNotificationsTypesRepository;
        this.iNotificationsRepository = iNotificationsRepository;
        this.iFeedClient = iFeedClient;
        this.iAccountsClient = iAccountsClient;
        this.iSseService = iSseService;
    }

    @Override
    public void createNewNotification(NewNotificationRequest request, String authorization) throws Exception {
        NotificationsTypes notificationType = iNotificationsTypesRepository.findByDescription(request.getTypeDescription())
                .orElseThrow(NotificationTypeNotFoundException::new);

        if (request.getTweetIdentifier() != null) {
            try {
                iFeedClient.getTweetDetails(request.getTweetIdentifier(), authorization, false);
            } catch (Exception e) {
                throw new TweetNotFoundException();
            }
        }

        try {
            iAccountsClient.getUserInfosByIdentifier(request.getUserSenderIdentifier(), authorization);
        } catch (Exception e) {
            throw new UserNotFoundException();
        }

        iNotificationsRepository.save(Notifications.builder()
                .identifier(UUID.randomUUID().toString())
                .tweetIdentifier(request.getTweetIdentifier())
                .userSenderIdentifier(request.getUserSenderIdentifier())
                .userReceiverIdentifier(request.getUserReceiverIdentifier())
                .typeIdentifier(notificationType.getTypeIdentifier())
                .visualized(false)
                .creationDate(UsefulDate.now())
                .build());

        iSseService.notifyFrontend(request.getUserReceiverIdentifier());
    }

    @Override
    public List<NotificationsResponse> getUserNotifications(Integer page, Integer size, String authorization, String userIdentifier) throws Exception {
        List<NotificationsResponse> response = new ArrayList<>();
        NotificationsTypes notificationsType;

        Page<Notifications> notificationsPage = iNotificationsRepository.findAllByUserReceiverIdentifier(userIdentifier, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "creationDate")));

        for (Notifications notification : notificationsPage.getContent()) {
            notificationsType = iNotificationsTypesRepository.findById(notification.getTypeIdentifier())
                    .orElseThrow(() -> new Exception("Notification type not found"));

            response.add(NotificationsResponse.builder()
                    .notificationIdentifier(notification.getIdentifier())
                    .typeDescription(notificationsType.getDescription())
                    .userResponse(iAccountsClient.getUserInfosByIdentifier(notification.getUserSenderIdentifier(), authorization))
                    .tweetResponse(notification.getTweetIdentifier() != null
                            ? iFeedClient.getTweetDetails(notification.getTweetIdentifier(), authorization, true)
                            : null)
                    .build());
        }
        return response;
    }

    @Override
    public void deleteNotification(DeleteNotificationRequest request) throws Exception {
        NotificationsTypes notificationType = iNotificationsTypesRepository.findByDescription(request.getTypeDescription())
                .orElseThrow(NotificationTypeNotFoundException::new);

        List<Notifications> notifications = iNotificationsRepository.findByUserSenderIdentifierAndUserReceiverIdentifierAndTypeIdentifierAndTweetIdentifier(
                request.getUserSenderIdentifier(),
                request.getUserReceiverIdentifier(),
                notificationType.getTypeIdentifier(),
                request.getTweetIdentifier()
        );

        notifications.stream().forEach(iNotificationsRepository::delete);
    }
}
