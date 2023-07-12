package br.com.souza.twitterclone.authentication.service.auth;

import br.com.souza.twitterclone.authentication.configuration.security.TokenProvider;
import br.com.souza.twitterclone.authentication.database.model.User;
import br.com.souza.twitterclone.authentication.dto.auth.LoginRequest;
import br.com.souza.twitterclone.authentication.dto.auth.TokenResponse;
import br.com.souza.twitterclone.authentication.handler.exceptions.BadCredencialsException;
import br.com.souza.twitterclone.authentication.handler.exceptions.GenericException;
import br.com.souza.twitterclone.authentication.handler.exceptions.NotConfirmedEmailException;
import br.com.souza.twitterclone.authentication.service.redis.RedisService;
import br.com.souza.twitterclone.authentication.service.user.UserService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;
    private final UserService userService;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 TokenProvider tokenProvider,
                                 RedisService redisService,
                                 UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.redisService = redisService;
        this.userService = userService;
    }

    public TokenResponse authenticate(final LoginRequest request) throws Exception {
        try{
            Authentication authentication = authenticationManager.authenticate(request.build());
            User user = tokenProvider.getUsuario(authentication);

            if(!userService.validateUserEmail(user)){
                throw new NotConfirmedEmailException();
            }

            TokenResponse response = (TokenResponse) redisService.getValue(TokenProvider.AUTH + user.getIdentifier(), TokenResponse.class);
            if(response == null){
                response = tokenProvider.generateToken(authentication);
                redisService.setValue(TokenProvider.AUTH + user.getIdentifier(), response, TimeUnit.MILLISECONDS, response.getExpiresIn(), true);
            }
            return response;
        }catch (AuthenticationException e){
            throw new BadCredencialsException();
        }catch (NotConfirmedEmailException e){
            throw new NotConfirmedEmailException();
        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }
}
