package br.com.souza.twitterclone.accounts.client;

import br.com.souza.twitterclone.accounts.configuration.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${endpoint.feed}", configuration = FeignConfiguration.class)
public interface IFeedClient {

    @GetMapping(value = "/feed/v1/posts/count/{userIdentifier}")
    Integer getTweetsCount(@PathVariable("userIdentifier") String userIdentifier);

}
