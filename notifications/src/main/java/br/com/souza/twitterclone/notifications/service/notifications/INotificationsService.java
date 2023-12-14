package br.com.souza.twitterclone.notifications.service.notifications;

import br.com.souza.twitterclone.notifications.dto.notifications.DeleteNotificationRequest;
import br.com.souza.twitterclone.notifications.dto.notifications.NewNotificationRequest;
import br.com.souza.twitterclone.notifications.dto.notifications.NotificationsResponse;
import java.util.List;
import org.springframework.data.domain.PageRequest;

public interface INotificationsService {

    void createNewNotification(NewNotificationRequest request) throws Exception;
    List<NotificationsResponse> getUserNotifications(Integer page, Integer size, String userIdentifier) throws Exception;
    void deleteNotification(DeleteNotificationRequest request) throws Exception;
}
