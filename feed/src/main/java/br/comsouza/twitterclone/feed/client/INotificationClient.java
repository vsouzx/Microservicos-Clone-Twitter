package br.comsouza.twitterclone.feed.client;

import br.comsouza.twitterclone.feed.configuration.feign.FeignConfiguration;
import br.comsouza.twitterclone.feed.dto.client.DeleteNotificationRequest;
import br.comsouza.twitterclone.feed.dto.client.NewNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${endpoint.notifications}", configuration = FeignConfiguration.class)
public interface INotificationClient {

    @PostMapping(value = "/notifications/v1")
    void createNewNotification(@RequestBody NewNotificationRequest request);

    @DeleteMapping(value = "/notifications/v1")
    void deleteNotification(@RequestBody DeleteNotificationRequest request);
}
