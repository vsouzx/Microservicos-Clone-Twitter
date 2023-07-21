package br.com.souza.twitterclone.accounts.controller.interactions.impl;

import br.com.souza.twitterclone.accounts.controller.interactions.IUserInteractionsController;
import br.com.souza.twitterclone.accounts.dto.user.UserPendingFollowRequest;
import br.com.souza.twitterclone.accounts.service.interactions.IUsersInteractionsService;
import br.com.souza.twitterclone.accounts.util.FindUserIdentifierHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/user/interactions")
public class UserInteractionsControllerImpl implements IUserInteractionsController {

    private final IUsersInteractionsService iUsersInteractionsService;

    public UserInteractionsControllerImpl(IUsersInteractionsService iUsersInteractionsService) {
        this.iUsersInteractionsService = iUsersInteractionsService;
    }

    @PatchMapping(value = "/blocktoggle/{identifier}")
    public ResponseEntity<Void> blockToggle(@PathVariable("identifier") String targetIdentifier) throws Exception {
        iUsersInteractionsService.blockToggle(FindUserIdentifierHelper.getIdentifier(), targetIdentifier);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/followtoggle/{identifier}")
    public ResponseEntity<Void> followToggle(@PathVariable("identifier") String targetIdentifier) throws Exception {
        iUsersInteractionsService.followToggle(FindUserIdentifierHelper.getIdentifier(), targetIdentifier);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/pendingfollow/{identifier}")
    public ResponseEntity<Void> pendingFollowAcceptDecline(@PathVariable("identifier") String targetIdentifier,
                                                           @RequestBody UserPendingFollowRequest request) throws Exception {
        iUsersInteractionsService.pendingFollowAcceptDecline(FindUserIdentifierHelper.getIdentifier(), targetIdentifier, request.isAccept());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/silencetoggle/{identifier}")
    public ResponseEntity<Void> silencetoggle(@PathVariable("identifier") String targetIdentifier) throws Exception {
        iUsersInteractionsService.silencetoggle(FindUserIdentifierHelper.getIdentifier(), targetIdentifier);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
