package br.com.souza.twitterclone.accounts.controller.interactions.impl;

import br.com.souza.twitterclone.accounts.controller.interactions.IUserInteractionsController;
import br.com.souza.twitterclone.accounts.dto.user.UserPendingFollowRequest;
import br.com.souza.twitterclone.accounts.service.interactions.IUsersInteractionsService;
import br.com.souza.twitterclone.accounts.service.redis.RedisService;
import br.com.souza.twitterclone.accounts.util.FindUserIdentifierHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/user/interactions")
public class UserInteractionsControllerImpl implements IUserInteractionsController {

    private final IUsersInteractionsService iUsersInteractionsService;
    private final RedisService redisService;

    public UserInteractionsControllerImpl(IUsersInteractionsService iUsersInteractionsService,
                                          RedisService redisService) {
        this.iUsersInteractionsService = iUsersInteractionsService;
        this.redisService = redisService;
    }

    @PatchMapping(value = "/blocktoggle/{identifier}")
    public ResponseEntity<Void> blockToggle(@PathVariable("identifier") String targetUserIdentifier) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersInteractionsService.blockToggle(FindUserIdentifierHelper.getIdentifier(), targetUserIdentifier);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/followtoggle/{identifier}")
    public ResponseEntity<Void> followToggle(@PathVariable("identifier") String targetIdentifier) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersInteractionsService.followToggle(FindUserIdentifierHelper.getIdentifier(), targetIdentifier);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/pendingfollow/{identifier}")
    public ResponseEntity<Void> pendingFollowAcceptDecline(@PathVariable("identifier") String targetIdentifier,
                                                           @RequestBody UserPendingFollowRequest request) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersInteractionsService.pendingFollowAcceptDecline(FindUserIdentifierHelper.getIdentifier(), targetIdentifier, request.isAccept());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/silencetoggle/{identifier}")
    public ResponseEntity<Void> silencetoggle(@PathVariable("identifier") String targetIdentifier) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersInteractionsService.silencetoggle(FindUserIdentifierHelper.getIdentifier(), targetIdentifier);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/alerttoggle/{identifier}")
    public ResponseEntity<Void> alertToggle(@PathVariable("identifier") String targetIdentifier) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersInteractionsService.alertToggle(FindUserIdentifierHelper.getIdentifier(), targetIdentifier);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/anyoneisblocked/{identifier}")
    public ResponseEntity<Boolean> anyoneIsBlocked(@PathVariable("identifier") String targetIdentifier) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        return new ResponseEntity<>(iUsersInteractionsService.anyoneIsBlocked(FindUserIdentifierHelper.getIdentifier(), targetIdentifier), HttpStatus.CREATED);
    }

    @GetMapping(value = "/isfollowing/{identifier}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable("identifier") String targetIdentifier) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        return new ResponseEntity<>(iUsersInteractionsService.verifyIfIsFollowing(FindUserIdentifierHelper.getIdentifier(), targetIdentifier).isPresent(), HttpStatus.CREATED);
    }

    @GetMapping(value = "/issilenced/{identifier}")
    public ResponseEntity<Boolean> isSilenced(@PathVariable("identifier") String targetIdentifier) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        return new ResponseEntity<>(iUsersInteractionsService.verifyIfIsSilenced(FindUserIdentifierHelper.getIdentifier(), targetIdentifier).isPresent(), HttpStatus.CREATED);
    }

}
