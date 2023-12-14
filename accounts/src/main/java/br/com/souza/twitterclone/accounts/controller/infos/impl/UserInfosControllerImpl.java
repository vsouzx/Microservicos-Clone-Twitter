package br.com.souza.twitterclone.accounts.controller.infos.impl;

import br.com.souza.twitterclone.accounts.controller.infos.IUserInfosController;
import br.com.souza.twitterclone.accounts.dto.user.ImageUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserEmailUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserInfosUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPasswordUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPrivacyUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserUsernameUpdateRequest;
import br.com.souza.twitterclone.accounts.service.infos.IUsersInfosService;
import br.com.souza.twitterclone.accounts.service.redis.RedisService;
import br.com.souza.twitterclone.accounts.util.FindUserIdentifierHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/user/infos")
public class UserInfosControllerImpl implements IUserInfosController {

    private final IUsersInfosService iUsersInfosService;
    private final RedisService redisService;

    public UserInfosControllerImpl(IUsersInfosService iUsersInfosService,
                                   RedisService redisService) {
        this.iUsersInfosService = iUsersInfosService;
        this.redisService = redisService;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUserInfos(@RequestBody UserInfosUpdateRequest request) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersInfosService.updateUserInfos(request, FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/email", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUserEmail(@RequestBody UserEmailUpdateRequest request) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersInfosService.updateUserEmail(request, FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/username", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUserUsername(@RequestBody UserUsernameUpdateRequest request) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersInfosService.updateUserUsername(request, FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUserPassword(@RequestBody UserPasswordUpdateRequest request) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersInfosService.updateUserPassword(request, FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/privacy", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUserPrivacy(@RequestBody UserPrivacyUpdateRequest request) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersInfosService.updateUserPrivacy(request, FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/profilephoto", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateProfilePhoto(@RequestBody ImageUpdateRequest request) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersInfosService.updateProfilePhoto(request, FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/backgroundphoto", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateBackgroundPhoto(@RequestBody ImageUpdateRequest request) throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersInfosService.updateBackgroundPhoto(request, FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/firstaccess", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateFirstAccessFlag() throws Exception {
        String userIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(userIdentifier);
        iUsersInfosService.updateFirstAccessFlag(FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
