package br.com.souza.twitterclone.accounts.client;

import br.com.souza.twitterclone.accounts.configuration.feign.FeignConfiguration;
import br.com.souza.twitterclone.accounts.dto.client.DeleteNotificationRequest;
import br.com.souza.twitterclone.accounts.dto.client.NewNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${endpoint.notifications}", configuration = FeignConfiguration.class)
public interface INotificationsClient {

    @PostMapping(value = "/notifications/v1")
    void createNewNotification(@RequestBody NewNotificationRequest request);

    @DeleteMapping(value = "/notifications/v1")
    void deleteNotification(@RequestBody DeleteNotificationRequest request);
}
