package br.com.souza.twitterclone.notifications.client;

import br.com.souza.twitterclone.notifications.configuration.openfeign.FeignClientConfiguration;
import br.com.souza.twitterclone.notifications.dto.client.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.notifications.handler.exceptions.ServerSideErrorException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${endpoint.account}", configuration = FeignClientConfiguration.class)
public interface IAccountsClient {

    @GetMapping(value = "/accounts/v1/user/search/byidentifier/{identifier}")
    UserDetailsByIdentifierResponse getUserInfosByIdentifier(@PathVariable("identifier") String identifier, @RequestHeader("Authorization") String authorization) throws ServerSideErrorException;

}
