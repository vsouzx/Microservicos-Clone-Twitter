package br.com.souza.twitterclone.notifications.controller.impl;

import br.com.souza.twitterclone.notifications.controller.INotificationsController;
import br.com.souza.twitterclone.notifications.dto.notifications.DeleteNotificationRequest;
import br.com.souza.twitterclone.notifications.dto.notifications.NewNotificationRequest;
import br.com.souza.twitterclone.notifications.dto.notifications.NotificationsResponse;
import br.com.souza.twitterclone.notifications.service.notifications.INotificationsService;
import br.com.souza.twitterclone.notifications.util.FindUserIdentifierHelper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(value = "/v1")
public class NotificationsControllerImpl implements INotificationsController {

    private final INotificationsService iNotificationsService;

    public NotificationsControllerImpl(INotificationsService iNotificationsService) {
        this.iNotificationsService = iNotificationsService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createNewNotification(@Valid @RequestBody NewNotificationRequest request,
                                                      @RequestHeader("Authorization") String authorization) throws Exception {
        iNotificationsService.createNewNotification(request, authorization);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NotificationsResponse>> getUserNotifications(@RequestHeader("Authorization") String authorization,
                                                                            @RequestParam("page") Integer page,
                                                                            @RequestParam("size") Integer size) throws Exception {
        return new ResponseEntity<>(iNotificationsService.getUserNotifications(page, size, authorization, FindUserIdentifierHelper.getIdentifier()), HttpStatus.CREATED);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteNotification(@RequestBody DeleteNotificationRequest request) throws Exception {
        iNotificationsService.deleteNotification(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
