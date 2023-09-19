package br.com.souza.twitterclone.notifications.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${endpoint.account}")
public interface IAccountsClient {

    //@GetMapping(value = "/accounts/v1/user/search/byidentifier/{identifier}")
    //UserDetailsByIdentifierResponse getUserInfosByIdentifier(@PathVariable("identifier") String identifier, @RequestHeader("Authorization") String authorization);

    //@GetMapping(value = "/accounts/v1/user/search")
    //UserDetailsResponse getUserDetails(@RequestHeader("Authorization") String authorization);
}