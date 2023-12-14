package br.com.souza.twitterclone.directmessages.client;

import br.com.souza.twitterclone.directmessages.configuration.feign.FeignConfiguration;
import br.com.souza.twitterclone.directmessages.dto.client.TimelineTweetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${endpoint.feed}", configuration = FeignConfiguration.class)
public interface IFeedClient {

    @GetMapping(value = "/feed/v1/posts/detail/{tweetIdentifier}")
    TimelineTweetResponse getTweetDetails(@PathVariable("tweetIdentifier") String tweetIdentifier, @RequestParam("load") Boolean load);

}
