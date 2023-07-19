package br.com.souza.twitterclone.accounts.controller.search.impl;

import br.com.souza.twitterclone.accounts.controller.search.IUserSearchController;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.service.search.IUsersSearchService;
import br.com.souza.twitterclone.accounts.util.FindUserIdentifierHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/user/search")
public class UserSearchControllerImpl implements IUserSearchController {

    private final IUsersSearchService iUsersSearchService;

    public UserSearchControllerImpl(IUsersSearchService iUsersSearchService) {
        this.iUsersSearchService = iUsersSearchService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsResponse> getUserInfos() throws Exception{
        return new ResponseEntity<>(iUsersSearchService.searchUserInfos(FindUserIdentifierHelper.getIdentifier()), HttpStatus.OK);
    }

    @GetMapping(value = "/{identifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsByIdentifierResponse> getUserInfosByIdentifier(@PathVariable("identifier") String targetUserIdentifier) throws Exception{
        return new ResponseEntity<>(iUsersSearchService.searchUserInfosByIdentifier(FindUserIdentifierHelper.getIdentifier(), targetUserIdentifier), HttpStatus.OK);
    }

    //TODO: Buscar lista de users por username
}
