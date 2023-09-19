package br.com.souza.twitterclone.notifications.service.notifications.impl;

import br.com.souza.twitterclone.notifications.client.IFeedClient;
import br.com.souza.twitterclone.notifications.database.model.Notifications;
import br.com.souza.twitterclone.notifications.database.model.NotificationsTypes;
import br.com.souza.twitterclone.notifications.database.repository.INotificationsRepository;
import br.com.souza.twitterclone.notifications.database.repository.INotificationsTypesRepository;
import br.com.souza.twitterclone.notifications.dto.client.TimelineTweetResponse;
import br.com.souza.twitterclone.notifications.dto.notifications.NewNotificationRequest;
import br.com.souza.twitterclone.notifications.handler.exceptions.ServerSideErrorException;
import br.com.souza.twitterclone.notifications.handler.exceptions.TweetNotFoundException;
import br.com.souza.twitterclone.notifications.service.notifications.INotificationsService;
import br.com.souza.twitterclone.notifications.util.UsefulDate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationsServiceImpl implements INotificationsService {

    private final INotificationsTypesRepository iNotificationsTypesRepository;
    private final INotificationsRepository iNotificationsRepository;
    private final IFeedClient iFeedClient;

    public NotificationsServiceImpl(INotificationsTypesRepository iNotificationsTypesRepository,
                                    INotificationsRepository iNotificationsRepository,
                                    IFeedClient iFeedClient) {
        this.iNotificationsTypesRepository = iNotificationsTypesRepository;
        this.iNotificationsRepository = iNotificationsRepository;
        this.iFeedClient = iFeedClient;
    }

    @Override
    public void createNewNotification(NewNotificationRequest request, String authorization) throws Exception {
        NotificationsTypes notificationType = iNotificationsTypesRepository.findByDescription(request.getTypeDescription())
                .orElseThrow(() -> new Exception("Notification type not found"));

        try{
            iFeedClient.getTweetDetails(request.getTweetIdentifier(), authorization, false);
        }catch (ServerSideErrorException e){
            throw new ServerSideErrorException();
        }
        catch (Exception e){
            throw new TweetNotFoundException();
        }

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
