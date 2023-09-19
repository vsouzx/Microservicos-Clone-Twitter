package br.com.souza.twitterclone.notifications.controller.impl;

import br.com.souza.twitterclone.notifications.controller.INotificationsController;
import br.com.souza.twitterclone.notifications.dto.notifications.NewNotificationRequest;
import br.com.souza.twitterclone.notifications.service.notifications.INotificationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1")
public class NotificationsControllerImpl implements INotificationsController {

    private final INotificationsService iNotificationsService;

    public NotificationsControllerImpl(INotificationsService iNotificationsService) {
        this.iNotificationsService = iNotificationsService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createNewNotification(@RequestBody NewNotificationRequest request,
                                                      @RequestHeader("Authorization") String authorization) throws Exception {
        iNotificationsService.createNewNotification(request, authorization);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
