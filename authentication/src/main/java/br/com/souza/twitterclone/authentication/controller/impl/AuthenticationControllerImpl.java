package br.com.souza.twitterclone.authentication.controller.impl;

import br.com.souza.twitterclone.authentication.controller.IAuthenticationController;
import br.com.souza.twitterclone.authentication.dto.auth.LoginRequest;
import br.com.souza.twitterclone.authentication.dto.auth.TokenResponse;
import br.com.souza.twitterclone.authentication.service.auth.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/connect")
public class AuthenticationControllerImpl implements IAuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationControllerImpl(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> authenticate(@RequestBody LoginRequest request) throws Exception{
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
    }
}
