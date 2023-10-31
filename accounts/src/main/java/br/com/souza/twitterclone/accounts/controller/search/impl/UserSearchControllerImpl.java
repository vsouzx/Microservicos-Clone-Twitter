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
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<UserDetailsResponse> getUserInfos(@RequestHeader("Authorization") String authorization) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.searchUserInfos(FindUserIdentifierHelper.getIdentifier(), authorization), HttpStatus.OK);
    }

    @GetMapping(value = "/byidentifier/{identifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsByIdentifierResponse> getUserInfosByIdentifier(@PathVariable("identifier") String targetUserIdentifier,
                                                                                    @RequestHeader("Authorization") String authorization) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.searchUserInfosByIdentifier(FindUserIdentifierHelper.getIdentifier(), targetUserIdentifier, authorization), HttpStatus.OK);
    }

    @GetMapping(value = "/byusername", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDetailsByIdentifierResponse>> getUsersByUsername(@RequestParam(value = "username", required = false) String targetUsername,
                                                                                    @RequestParam(value = "page", required = true) Integer page,
                                                                                    @RequestParam(value = "size", required = true) Integer size,
                                                                                    @RequestHeader("Authorization") String authorization) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.getUsersByUsername(FindUserIdentifierHelper.getIdentifier(), targetUsername, page, size, authorization), HttpStatus.OK);
    }

    @GetMapping(value = "/followsdetails/{identifier}/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDetailsByIdentifierResponse>> getUserFollowsDetails(@PathVariable("identifier") String targetUserIdentifier,
                                                                                       @PathVariable("type") String type,
                                                                                       @RequestParam(value = "page", required = true) Integer page,
                                                                                       @RequestParam(value = "size", required = true) Integer size,
                                                                                       @RequestHeader("Authorization") String authorization) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.getUserFollowsDetails(FindUserIdentifierHelper.getIdentifier(), targetUserIdentifier, type, page, size, authorization), HttpStatus.OK);
    }

    @GetMapping(value = "/pendingfollowers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserPreviewResponse>> getUserPendingFollowers(@RequestParam(value = "page", required = true) Integer page,
                                                                             @RequestParam(value = "size", required = true) Integer size) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.getUserPendingFollowers(FindUserIdentifierHelper.getIdentifier(), page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/isvalidemail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidEmailResponse> isValidEmail(@RequestParam(value = "email", required = true) String email) {
        return new ResponseEntity<>(iUsersSearchService.isValidEmail(email), HttpStatus.OK);
    }

    @GetMapping(value = "/isvalidusername", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidUsernameResponse> isValidUsername(@RequestParam(value = "username", required = true) String username) {
        return new ResponseEntity<>(iUsersSearchService.isValidUsername(username), HttpStatus.OK);
    }

    @GetMapping(value = "/isvaliduser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidUserResponse> isValidUser(@RequestParam(value = "username", required = true) String username) {
        return new ResponseEntity<>(iUsersSearchService.isValidUser(username), HttpStatus.OK);
    }

    @GetMapping(value = "/whotofollow", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDetailsByIdentifierResponse>> getWhoToFollow(@RequestParam(value = "page", required = true) Integer page,
                                                                                @RequestParam(value = "size", required = true) Integer size,
                                                                                @RequestParam(value = "userOnScreen", required = false) String userOnScreen,
                                                                                @RequestParam(value = "isVerified", required = false) Boolean isVerified,
                                                                                @RequestHeader("Authorization") String authorization) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.getWhoToFollow(FindUserIdentifierHelper.getIdentifier(), page, size, userOnScreen, isVerified, authorization), HttpStatus.OK);
    }

    @GetMapping(value = "/verified", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserPreviewResponse>> getVerified() {
        return new ResponseEntity<>(iUsersSearchService.getVerified(), HttpStatus.OK);
    }

    @GetMapping(value = "/alertedusers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAlertedUsers() throws Exception {
        return new ResponseEntity<>(iUsersSearchService.getAlertedUsers(FindUserIdentifierHelper.getIdentifier()), HttpStatus.OK);
    }

    @GetMapping(value = "/followsandfollowers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FollowsAndFollowersResponse> getFollowsAndFollowers(@RequestParam(value = "targetUserIdentifier") String targetUserIdentifer) {
        return new ResponseEntity<>(iUsersSearchService.getFollowsAndFollowers(targetUserIdentifer), HttpStatus.OK);
    }

    @GetMapping(value = "/allknownfollowers/{targetUserIdentifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<KnownUsersResponse>> getAllKnownFollowers(@PathVariable("targetUserIdentifier") String targetUserIdentifier,
                                                                         @RequestHeader("Authorization") String authorization) throws Exception {
        return new ResponseEntity<>(iUsersSearchService.getAllKnownFollowers(FindUserIdentifierHelper.getIdentifier(), targetUserIdentifier, authorization), HttpStatus.OK);
    }
}
