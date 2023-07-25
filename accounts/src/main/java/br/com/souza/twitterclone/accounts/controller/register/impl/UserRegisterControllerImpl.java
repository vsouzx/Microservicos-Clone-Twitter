package br.com.souza.twitterclone.accounts.controller.register.impl;

import br.com.souza.twitterclone.accounts.controller.register.IUserRegisterController;
import br.com.souza.twitterclone.accounts.service.register.IUsersRegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/v1/user/register")
public class UserRegisterControllerImpl implements IUserRegisterController {

    private final IUsersRegisterService iUsersRegisterService;

    public UserRegisterControllerImpl(IUsersRegisterService iUsersRegisterService) {
        this.iUsersRegisterService = iUsersRegisterService;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> userRegister(@RequestPart("request") String request,
                                             @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) throws Exception {
        iUsersRegisterService.userRegister(request, profilePhoto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/resendcode", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> resendConfirmationCode(@RequestParam("email") String email) throws Exception {
        iUsersRegisterService.resendConfirmationCode(email);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
