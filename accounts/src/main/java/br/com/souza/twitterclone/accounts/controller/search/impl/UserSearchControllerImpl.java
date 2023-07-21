package br.com.souza.twitterclone.accounts.controller.search.impl;

import br.com.souza.twitterclone.accounts.controller.search.IUserSearchController;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import br.com.souza.twitterclone.accounts.service.search.IUsersSearchService;
import br.com.souza.twitterclone.accounts.util.FindUserIdentifierHelper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping(value = "/byidentifier/{identifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsByIdentifierResponse> getUserInfosByIdentifier(@PathVariable("identifier") String targetUserIdentifier) throws Exception{
        return new ResponseEntity<>(iUsersSearchService.searchUserInfosByIdentifier(FindUserIdentifierHelper.getIdentifier(), targetUserIdentifier), HttpStatus.OK);
    }

     @GetMapping(value = "/byusername", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserPreviewResponse>> getUsersByUsername(@RequestParam(value = "username", required = false) String targetUsername) throws Exception{
        return new ResponseEntity<>(iUsersSearchService.getUsersByUsername(FindUserIdentifierHelper.getIdentifier(), targetUsername), HttpStatus.OK);
    }

    //TODO: Buscar lista de follows de um usuario

    //TODO: Buscar lista de followers de um usuario

    //TODO: Buscar lista de pending followers de um usuario
}
