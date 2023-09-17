package br.com.souza.twitterclone.notifications.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${endpoint.feed}")
public interface IFeedClient {

    //@GetMapping(value = "/feed/v1/posts/detail/{tweetIdentifier}")
    //TimelineTweetResponse getTweetDetails(@PathVariable("identifier") String identifier, @RequestHeader("Authorization") String authorization);

}
