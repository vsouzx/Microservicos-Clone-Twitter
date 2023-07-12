package br.com.souza.twitterclone.authentication.service.auth;

import br.com.souza.twitterclone.authentication.configuration.TokenProvider;
import br.com.souza.twitterclone.authentication.dto.auth.LoginRequest;
import br.com.souza.twitterclone.authentication.dto.auth.TokenResponse;
import br.com.souza.twitterclone.authentication.handler.exceptions.BadCredencialsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AuthenticationService {

    final AuthenticationManager authenticationManager;
    final TokenProvider tokenProvider;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public TokenResponse authenticate(final LoginRequest request) throws Exception {
        try{
            Authentication authentication = authenticationManager.authenticate(request.build());
            return tokenProvider.generateToken(authentication);
        }catch (Exception e){
            log.debug("Bad credentials");
            throw new BadCredencialsException("Bad credentials");
        }
    }
}
