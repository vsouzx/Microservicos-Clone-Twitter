package br.com.souza.twitterclone.notifications.service.notifications.impl;

import br.com.souza.twitterclone.notifications.client.IAccountsClient;
import br.com.souza.twitterclone.notifications.client.IFeedClient;
import br.com.souza.twitterclone.notifications.database.model.Notifications;
import br.com.souza.twitterclone.notifications.database.model.NotificationsTypes;
import br.com.souza.twitterclone.notifications.database.repository.INotificationsRepository;
import br.com.souza.twitterclone.notifications.database.repository.INotificationsTypesRepository;
import br.com.souza.twitterclone.notifications.dto.notifications.NewNotificationRequest;
import br.com.souza.twitterclone.notifications.handler.exceptions.ServerSideErrorException;
import br.com.souza.twitterclone.notifications.handler.exceptions.TweetNotFoundException;
import br.com.souza.twitterclone.notifications.handler.exceptions.NotificationTypeNotFoundException;
import br.com.souza.twitterclone.notifications.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.notifications.service.notifications.INotificationsService;
import br.com.souza.twitterclone.notifications.util.UsefulDate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationsServiceImpl implements INotificationsService {

    private final INotificationsTypesRepository iNotificationsTypesRepository;
    private final INotificationsRepository iNotificationsRepository;
    private final IFeedClient iFeedClient;
    private final IAccountsClient iAccountsClient;

    public NotificationsServiceImpl(INotificationsTypesRepository iNotificationsTypesRepository,
                                    INotificationsRepository iNotificationsRepository,
                                    IFeedClient iFeedClient,
                                    IAccountsClient iAccountsClient) {
        this.iNotificationsTypesRepository = iNotificationsTypesRepository;
        this.iNotificationsRepository = iNotificationsRepository;
        this.iFeedClient = iFeedClient;
        this.iAccountsClient = iAccountsClient;
    }

    @Override
    public void createNewNotification(NewNotificationRequest request, String authorization) throws Exception {
        NotificationsTypes notificationType = iNotificationsTypesRepository.findByDescription(request.getTypeDescription())
                .orElseThrow(NotificationTypeNotFoundException::new);

        if(request.getTweetIdentifier() != null){
            try{
                iFeedClient.getTweetDetails(request.getTweetIdentifier(), authorization, false);
            }catch (ServerSideErrorException e){
                throw new ServerSideErrorException();
            } catch (Exception e){
                throw new TweetNotFoundException();
            }
        }

        try {
            iAccountsClient.getUserInfosByIdentifier(request.getUserIdentifier(), authorization);
        }catch (ServerSideErrorException e){
            throw new ServerSideErrorException();
        } catch (Exception e){
            throw new UserNotFoundException();
        }

        iNotificationsRepository.save(Notifications.builder()
                .identifier(UUID.randomUUID().toString())
                .tweetIdentifier(request.getTweetIdentifier())
                .userIdentifier(request.getUserIdentifier())
                .typeIdentifier(notificationType.getTypeIdentifier())
                .visualized(false)
                .creationDate(UsefulDate.now())
                .build());

        //TODO: Após criar, notificar via SSE
    }
}
