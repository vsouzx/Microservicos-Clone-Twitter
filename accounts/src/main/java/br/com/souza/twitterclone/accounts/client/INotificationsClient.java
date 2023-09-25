package br.com.souza.twitterclone.accounts.client;

import br.com.souza.twitterclone.accounts.dto.client.NewNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${endpoint.notifications}")
public interface INotificationsClient {

    @PostMapping(value = "/notifications/v1")
    void createNewNotification(@RequestBody NewNotificationRequest request, @RequestHeader("Authorization") String authorization);
}
