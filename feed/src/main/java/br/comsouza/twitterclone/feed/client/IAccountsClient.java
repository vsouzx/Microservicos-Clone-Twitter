package br.comsouza.twitterclone.feed.client;

import br.comsouza.twitterclone.feed.dto.client.ProfilePhotoResponse;
import br.comsouza.twitterclone.feed.dto.client.UserDetailsByIdentifierResponse;
import br.comsouza.twitterclone.feed.dto.client.UserDetailsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${endpoint.account}")
public interface IAccountsClient {

    @GetMapping(value = "/accounts/v1/user/search/byidentifier/{identifier}")
    UserDetailsByIdentifierResponse getUserInfosByIdentifier(@PathVariable("identifier") String identifier, @RequestHeader("Authorization") String authorization);

    @GetMapping(value = "/accounts/v1/user/search")
    UserDetailsResponse getUserDetails(@RequestHeader("Authorization") String authorization);

    @GetMapping(value = "/accounts/v1/user/search/profilephoto")
    ProfilePhotoResponse loadProfilePhoto(@RequestParam("profilePhotoIdentifier") String userIdeprofilePhotoIdentifierntifier, @RequestHeader("Authorization") String authorization);
}
