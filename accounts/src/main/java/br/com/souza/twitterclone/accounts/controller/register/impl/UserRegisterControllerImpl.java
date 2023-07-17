package br.com.souza.twitterclone.accounts.controller.register.impl;

import br.com.souza.twitterclone.accounts.controller.register.IUserRegisterController;
import br.com.souza.twitterclone.accounts.dto.user.UserRegistrationRequest;
import br.com.souza.twitterclone.accounts.service.register.IUsersRegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/user/register")
public class UserRegisterControllerImpl implements IUserRegisterController {

    private final IUsersRegisterService iUsersRegisterService;

    public UserRegisterControllerImpl(IUsersRegisterService iUsersRegisterService) {
        this.iUsersRegisterService = iUsersRegisterService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> userRegister(@RequestBody UserRegistrationRequest request) throws Exception{
        iUsersRegisterService.userRegister(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
