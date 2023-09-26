package br.com.souza.twitterclone.accounts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${endpoint.feed}")
public interface IFeedClient {

    @GetMapping(value = "/feed/v1/posts/count/{userIdentifier}")
    Integer getTweetsCount(@PathVariable("userIdentifier") String userIdentifier, @RequestHeader("Authorization") String authorization);

}
