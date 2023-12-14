package br.com.souza.twitterclone.notifications.controller.impl;

import br.com.souza.twitterclone.notifications.controller.INotificationsController;
import br.com.souza.twitterclone.notifications.dto.notifications.DeleteNotificationRequest;
import br.com.souza.twitterclone.notifications.dto.notifications.NewNotificationRequest;
import br.com.souza.twitterclone.notifications.dto.notifications.NotificationsResponse;
import br.com.souza.twitterclone.notifications.service.notifications.INotificationsService;
import br.com.souza.twitterclone.notifications.service.redis.RedisService;
import br.com.souza.twitterclone.notifications.util.FindUserIdentifierHelper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(value = "/v1")
public class NotificationsControllerImpl implements INotificationsController {

    private final INotificationsService iNotificationsService;
    private final RedisService redisService;

    public NotificationsControllerImpl(INotificationsService iNotificationsService,
                                       RedisService redisService) {
        this.iNotificationsService = iNotificationsService;
        this.redisService = redisService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createNewNotification(@RequestBody NewNotificationRequest request) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        iNotificationsService.createNewNotification(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NotificationsResponse>> getUserNotifications(@RequestParam("page") Integer page,
                                                                            @RequestParam("size") Integer size) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        return new ResponseEntity<>(iNotificationsService.getUserNotifications(page, size, sessionUserIdentifier), HttpStatus.CREATED);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteNotification(@RequestBody DeleteNotificationRequest request) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        iNotificationsService.deleteNotification(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
