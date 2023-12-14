package br.comsouza.twitterclone.feed.client;

import br.comsouza.twitterclone.feed.configuration.feign.FeignConfiguration;
import br.comsouza.twitterclone.feed.dto.client.UserDetailsByIdentifierResponse;
import br.comsouza.twitterclone.feed.dto.client.UserDetailsResponse;
import br.comsouza.twitterclone.feed.dto.posts.ProfilePhotoResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${endpoint.account}", configuration = FeignConfiguration.class)
public interface IAccountsClient {

    @GetMapping(value = "/accounts/v1/user/search/byidentifier/{identifier}")
    UserDetailsByIdentifierResponse getUserInfosByIdentifier(@PathVariable("identifier") String identifier);

    @GetMapping(value = "/accounts/v1/user/search")
    UserDetailsResponse getUserDetails();

    @GetMapping(value = "/accounts/v1/user/search/alertedusers")
    List<String> getAlertedUsers();
}
