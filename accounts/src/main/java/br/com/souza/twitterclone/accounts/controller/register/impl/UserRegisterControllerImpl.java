package br.com.souza.twitterclone.accounts.controller.register.impl;

import br.com.souza.twitterclone.accounts.controller.register.IUserRegisterController;
import br.com.souza.twitterclone.accounts.dto.user.UserRegistrationRequest;
import br.com.souza.twitterclone.accounts.service.register.IUsersRegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<Void> userRegister(@RequestBody UserRegistrationRequest request) throws Exception {
        iUsersRegisterService.userRegister(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/sendcode")
    public ResponseEntity<Void> sendConfirmationCode(@RequestParam("email") String email) throws Exception {
        iUsersRegisterService.sendConfirmationCode(email);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/confirmcode", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmCode(@RequestParam("email") String email,
                                            @RequestParam("code") String code) throws Exception {
        iUsersRegisterService.confirmCode(email, code);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
