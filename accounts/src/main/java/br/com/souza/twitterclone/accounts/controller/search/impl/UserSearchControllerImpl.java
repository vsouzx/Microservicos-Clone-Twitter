package br.com.souza.twitterclone.accounts.controller.search.impl;

import br.com.souza.twitterclone.accounts.controller.search.IUserSearchController;
import br.com.souza.twitterclone.accounts.dto.user.FollowsAndFollowersResponse;
import br.com.souza.twitterclone.accounts.dto.user.KnownUsersResponse;
import br.com.souza.twitterclone.accounts.dto.user.SaveHistoricRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserSearchHistoricResponse;
import br.com.souza.twitterclone.accounts.dto.user.ValidEmailResponse;
import br.com.souza.twitterclone.accounts.dto.user.ValidUserResponse;
import br.com.souza.twitterclone.accounts.dto.user.ValidUsernameResponse;
import br.com.souza.twitterclone.accounts.service.redis.RedisService;
import br.com.souza.twitterclone.accounts.service.search.IUsersSearchService;
import br.com.souza.twitterclone.accounts.util.FindUserIdentifierHelper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/user/search")
public class UserSearchControllerImpl implements IUserSearchController {

    private final IUsersSearchService iUsersSearchService;
    private final RedisService redisService;

    public UserSearchControllerImpl(IUsersSearchService iUsersSearchService, RedisService redisService) {
        this.iUsersSearchService = iUsersSearchService;
        this.redisService = redisService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsResponse> getUserInfos() throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        return new ResponseEntity<>(iUsersSearchService.searchUserInfos(userIdentifier), HttpStatus.OK);
    }

    @GetMapping(value = "/byidentifier/{identifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsByIdentifierResponse> getUserInfosByIdentifier(@PathVariable("identifier") String targetUserIdentifier) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        return new ResponseEntity<>(iUsersSearchService.searchUserInfosByIdentifier(userIdentifier, targetUserIdentifier), HttpStatus.OK);
    }

    @GetMapping(value = "/byusername", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDetailsByIdentifierResponse>> getUsersByUsername(@RequestParam(value = "username", required = false) String targetUsername,
                                                                                    @RequestParam(value = "page", required = true) Integer page,
                                                                                    @RequestParam(value = "size", required = true) Integer size) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        return new ResponseEntity<>(iUsersSearchService.getUsersByUsername(FindUserIdentifierHelper.getIdentifier(), targetUsername, page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/followsdetails/{identifier}/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDetailsByIdentifierResponse>> getUserFollowsDetails(@PathVariable("identifier") String targetUserIdentifier,
                                                                                       @PathVariable("type") String type,
                                                                                       @RequestParam(value = "page", required = true) Integer page,
                                                                                       @RequestParam(value = "size", required = true) Integer size) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        return new ResponseEntity<>(iUsersSearchService.getUserFollowsDetails(FindUserIdentifierHelper.getIdentifier(), targetUserIdentifier, type, page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/pendingfollowers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserPreviewResponse>> getUserPendingFollowers(@RequestParam(value = "page", required = true) Integer page,
                                                                             @RequestParam(value = "size", required = true) Integer size) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        return new ResponseEntity<>(iUsersSearchService.getUserPendingFollowers(FindUserIdentifierHelper.getIdentifier(), page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/isvalidemail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidEmailResponse> isValidEmail(@RequestParam(value = "email", required = true) String email){
        return new ResponseEntity<>(iUsersSearchService.isValidEmail(email), HttpStatus.OK);
    }

    @GetMapping(value = "/isvalidusername", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidUsernameResponse> isValidUsername(@RequestParam(value = "username", required = true) String username){
        return new ResponseEntity<>(iUsersSearchService.isValidUsername(username), HttpStatus.OK);
    }

    @GetMapping(value = "/isvaliduser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidUserResponse> isValidUser(@RequestParam(value = "username", required = true) String username){
        return new ResponseEntity<>(iUsersSearchService.isValidUser(username), HttpStatus.OK);
    }

    @GetMapping(value = "/whotofollow", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDetailsByIdentifierResponse>> getWhoToFollow(@RequestParam(value = "page", required = true) Integer page,
                                                                                @RequestParam(value = "size", required = true) Integer size,
                                                                                @RequestParam(value = "userOnScreen", required = false) String userOnScreen,
                                                                                @RequestParam(value = "isVerified", required = false) Boolean isVerified) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        return new ResponseEntity<>(iUsersSearchService.getWhoToFollow(FindUserIdentifierHelper.getIdentifier(), page, size, userOnScreen, isVerified), HttpStatus.OK);
    }

    @GetMapping(value = "/verified", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserPreviewResponse>> getVerified() throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        return new ResponseEntity<>(iUsersSearchService.getVerified(), HttpStatus.OK);
    }

    @GetMapping(value = "/alertedusers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAlertedUsers() throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        return new ResponseEntity<>(iUsersSearchService.getAlertedUsers(FindUserIdentifierHelper.getIdentifier()), HttpStatus.OK);
    }

    @GetMapping(value = "/followsandfollowers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FollowsAndFollowersResponse> getFollowsAndFollowers(@RequestParam(value = "targetUserIdentifier") String targetUserIdentifer) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        return new ResponseEntity<>(iUsersSearchService.getFollowsAndFollowers(targetUserIdentifer), HttpStatus.OK);
    }

    @GetMapping(value = "/allknownfollowers/{targetUserIdentifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<KnownUsersResponse>> getAllKnownFollowers(@PathVariable("targetUserIdentifier") String targetUserIdentifier) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        return new ResponseEntity<>(iUsersSearchService.getAllKnownFollowers(FindUserIdentifierHelper.getIdentifier(), targetUserIdentifier), HttpStatus.OK);
    }

    @GetMapping(value = "/historic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserSearchHistoricResponse>> getUserSearchHistoric() throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        return new ResponseEntity<>(iUsersSearchService.getUserSearchHistoric(FindUserIdentifierHelper.getIdentifier()), HttpStatus.OK);
    }

    @PostMapping(value = "/historic", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveUserSearchHistoric(@RequestBody SaveHistoricRequest request) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersSearchService.saveUserSearchHistoric(FindUserIdentifierHelper.getIdentifier(), request.getTargetUserIdentifier(), request.getText());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/historic/{historicIdentifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUserSearchHistoric(@PathVariable("historicIdentifier") String historicIdentifier) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersSearchService.deleteUserSearchHistoric(historicIdentifier);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/historic/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAllUserSearchHistoric() throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersSearchService.deleteAllUserSearchHistoric(FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
