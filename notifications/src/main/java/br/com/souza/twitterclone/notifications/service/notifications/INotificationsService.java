package br.com.souza.twitterclone.notifications.service.notifications;

import br.com.souza.twitterclone.notifications.dto.notifications.NewNotificationRequest;
import br.com.souza.twitterclone.notifications.dto.notifications.NotificationsResponse;
import java.util.List;
import org.springframework.data.domain.PageRequest;

public interface INotificationsService {

    void createNewNotification(NewNotificationRequest request, String authorization) throws Exception;
    List<NotificationsResponse> getUserNotifications(PageRequest request, String authorization, String userIdentifier) throws Exception;
}
