package br.comsouza.twitterclone.feed.client;

import br.comsouza.twitterclone.feed.dto.client.GPTRequest;
import br.comsouza.twitterclone.feed.dto.client.GPTResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "openapiGPT", url = "https://api.openai.com/v1/engines/text-davinci-003/completions")
public interface IChatGPTClient {

    @PostMapping
    GPTResponse generate(@RequestHeader(value = "Authorization") String authorizationHeader, @RequestBody GPTRequest request);
}
