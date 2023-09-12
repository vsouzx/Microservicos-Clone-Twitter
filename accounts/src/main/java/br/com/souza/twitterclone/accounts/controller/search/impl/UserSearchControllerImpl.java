package br.com.souza.twitterclone.accounts.controller.search.impl;

import br.com.souza.twitterclone.accounts.controller.search.IUserSearchController;
import br.com.souza.twitterclone.accounts.dto.user.*;
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
    public ResponseEntity<UserDetailsResponse> getUserInfos() throws Exception {
        return new ResponseEntity<>(iUsersSearchService.searchUserInfos(FindUserIdentifierHelper.getIdentifier()), HttpStatus.OK);
    }

    @GetMapping(value = "/byidentifier/{identifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsByIdentifierResponse> getUserInfosByIdentifier(@PathVariable("identifier") String targetUserIdentifier) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.searchUserInfosByIdentifier(FindUserIdentifierHelper.getIdentifier(), targetUserIdentifier), HttpStatus.OK);
    }

    @GetMapping(value = "/byusername", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserPreviewResponse>> getUsersByUsername(@RequestParam(value = "username", required = false) String targetUsername,
                                                                        @RequestParam(value = "page", required = true) Integer page,
                                                                        @RequestParam(value = "size", required = true) Integer size) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.getUsersByUsername(FindUserIdentifierHelper.getIdentifier(), targetUsername, page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/followers/{identifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserPreviewResponse>> getUserFollowers(@PathVariable("identifier") String targetUserIdentifier,
                                                                      @RequestParam(value = "page", required = true) Integer page,
                                                                      @RequestParam(value = "size", required = true) Integer size) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.getUserFollowers(FindUserIdentifierHelper.getIdentifier(), targetUserIdentifier, page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/follows/{identifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserPreviewResponse>> getUserFollows(@PathVariable("identifier") String targetUserIdentifier,
                                                                    @RequestParam(value = "page", required = true) Integer page,
                                                                    @RequestParam(value = "size", required = true) Integer size) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.getUserFollows(FindUserIdentifierHelper.getIdentifier(), targetUserIdentifier, page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/pendingfollowers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserPreviewResponse>> getUserPendingFollowers(@RequestParam(value = "page", required = true) Integer page,
                                                                             @RequestParam(value = "size", required = true) Integer size) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.getUserPendingFollowers(FindUserIdentifierHelper.getIdentifier(), page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/isvalidemail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidEmailResponse> isValidEmail(@RequestParam(value = "email", required = true) String email) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.isValidEmail(email), HttpStatus.OK);
    }

    @GetMapping(value = "/isvalidusername", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidUsernameResponse> isValidUsername(@RequestParam(value = "username", required = true) String username) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.isValidUsername(username), HttpStatus.OK);
    }

    @GetMapping(value = "/isvaliduser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidUserResponse> isValidUser(@RequestParam(value = "username", required = true) String username) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.isValidUser(username), HttpStatus.OK);
    }

    //TODO: Procurar 3 usuários aleatórios para seguir

}
