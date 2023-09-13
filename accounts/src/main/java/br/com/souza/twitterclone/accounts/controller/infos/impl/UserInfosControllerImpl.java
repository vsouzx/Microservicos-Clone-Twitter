package br.com.souza.twitterclone.accounts.controller.infos.impl;

import br.com.souza.twitterclone.accounts.controller.infos.IUserInfosController;
import br.com.souza.twitterclone.accounts.dto.user.ImageUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserEmailUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserInfosUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPasswordUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPrivacyUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserUsernameUpdateRequest;
import br.com.souza.twitterclone.accounts.service.infos.IUsersInfosService;
import br.com.souza.twitterclone.accounts.util.FindUserIdentifierHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/v1/user/infos")
public class UserInfosControllerImpl implements IUserInfosController {

    private final IUsersInfosService iUsersInfosService;

    public UserInfosControllerImpl(IUsersInfosService iUsersInfosService) {
        this.iUsersInfosService = iUsersInfosService;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUserInfos(@RequestBody UserInfosUpdateRequest request) throws Exception {
        iUsersInfosService.updateUserInfos(request, FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/email", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUserEmail(@RequestBody UserEmailUpdateRequest request, @RequestHeader("Authorization") String authorization) throws Exception {
        iUsersInfosService.updateUserEmail(request, FindUserIdentifierHelper.getIdentifier(), authorization);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/username", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUserUsername(@RequestBody UserUsernameUpdateRequest request) throws Exception {
        iUsersInfosService.updateUserUsername(request, FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUserPassword(@RequestBody UserPasswordUpdateRequest request, @RequestHeader("Authorization") String authorization) throws Exception {
        iUsersInfosService.updateUserPassword(request, FindUserIdentifierHelper.getIdentifier(), authorization);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/privacy", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUserPrivacy(@RequestBody UserPrivacyUpdateRequest request) throws Exception {
        iUsersInfosService.updateUserPrivacy(request, FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/profilephoto", consumes = {"multipart/form-data"})
    public ResponseEntity<Void> updateProfilePhoto(@RequestPart("profilePhoto") MultipartFile file) throws Exception {
        iUsersInfosService.updateProfilePhoto(file, FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/backgroundphoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateBackgroundPhoto(@RequestPart("backgroundPhoto") MultipartFile file,
                                                      @RequestPart("xPosition") String xPosition,
                                                      @RequestPart("yPosition") String yPosition) throws Exception {
        iUsersInfosService.updateBackgroundPhoto(file, FindUserIdentifierHelper.getIdentifier(), Integer.valueOf(xPosition), Integer.valueOf(yPosition));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/firstaccess", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateFirstAccessFlag() throws Exception {
        iUsersInfosService.updateFirstAccessFlag(FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
