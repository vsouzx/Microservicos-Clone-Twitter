package br.com.souza.twitterclone.accounts.service.infos.impl;

import br.com.souza.twitterclone.accounts.configuration.security.TokenProvider;
import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.dto.user.UserEmailUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserInfosUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPasswordUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPrivacyUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserUsernameUpdateRequest;
import br.com.souza.twitterclone.accounts.handler.exceptions.ActualPasswordNotMatchesException;
import br.com.souza.twitterclone.accounts.handler.exceptions.EmailAlreadyExistsException;
import br.com.souza.twitterclone.accounts.handler.exceptions.InvalidPasswordException;
import br.com.souza.twitterclone.accounts.handler.exceptions.InvalidUsernameRegexException;
import br.com.souza.twitterclone.accounts.handler.exceptions.NewPasswordConfirmationNotMatchesNullException;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.accounts.handler.exceptions.UsernameAlreadyExistsException;
import br.com.souza.twitterclone.accounts.service.infos.IUsersInfosService;
import br.com.souza.twitterclone.accounts.service.redis.RedisService;
import br.com.souza.twitterclone.accounts.util.PasswordValidatorHelper;
import br.com.souza.twitterclone.accounts.util.UsernameValidatorHelper;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsersInfosServiceImpl implements IUsersInfosService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final TokenProvider tokenProvider;

    public UsersInfosServiceImpl(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 RedisService redisService,
                                 TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.tokenProvider = tokenProvider;
    }

    public void updateUserInfos(UserInfosUpdateRequest request, String identifier) throws Exception {
        Optional<User> user = userRepository.findById(identifier);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        user.get().setFirstName(request.getFirstName());
        user.get().setBiography(request.getBiography());
        user.get().setLocation(request.getLocation());
        user.get().setSite(request.getSite());

        userRepository.save(user.get());
    }

    public void updateUserEmail(UserEmailUpdateRequest request, String identifier, String authorization) throws Exception {
        Optional<User> possibleUser = Optional.empty();
        Optional<User> user = userRepository.findById(identifier);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if(request.getEmail().equals(user.get().getEmail())){
            return;
        }

        possibleUser = userRepository.findByEmail(request.getEmail());

        if(possibleUser.isPresent()){
            throw new EmailAlreadyExistsException();
        }

        user.get().setConfirmedEmail(false);
        user.get().setConfirmationCode(null);
        user.get().setEmail(request.getEmail());

        //remover autenticacao do usuário do Redis, obrigando-o a se logar novamente
        String sessionId = tokenProvider.getSessionIdentifierFromToken(authorization);
        redisService.removeKey(sessionId);
        redisService.removeKey(TokenProvider.AUTH + identifier);

        //TODO: enviar mensagem para servico de email pelo kafka

        userRepository.save(user.get());
    }

    public void updateUserUsername(UserUsernameUpdateRequest request, String identifier) throws Exception {
        Optional<User> possibleUser = Optional.empty();
        Optional<User> user = userRepository.findById(identifier);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if(request.getUsername().equals(user.get().getUsername())){
            return;
        }
        if(UsernameValidatorHelper.isValidUsername(request.getUsername())){
            throw new InvalidUsernameRegexException();
        }

        possibleUser = userRepository.findByUsername(request.getUsername());
        if(possibleUser.isPresent()){
            throw new UsernameAlreadyExistsException();
        }

        user.get().setUsername(request.getUsername());
        userRepository.save(user.get());
    }

    public void updateUserPassword(UserPasswordUpdateRequest request, String identifier, String authorization) throws Exception {
        Optional<User> user = userRepository.findById(identifier);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if(!PasswordValidatorHelper.isValidPassword(request.getNewPassword())){
            throw new InvalidPasswordException();
        }
        if (!passwordEncoder.matches(request.getActualPassword(), user.get().getPassword())) {
            throw new ActualPasswordNotMatchesException();
        }
        if (!request.getNewPassword().equals(request.getNewPasswordConfirmation())) {
            throw new NewPasswordConfirmationNotMatchesNullException();
        }

        user.get().setPassword(passwordEncoder.encode(request.getNewPassword()));

        //remover autenticacao do usuário do Redis, obrigando-o a se logar novamente
        String sessionId = tokenProvider.getSessionIdentifierFromToken(authorization);
        redisService.removeKey(sessionId);
        redisService.removeKey(TokenProvider.AUTH + identifier);

        userRepository.save(user.get());
    }

    public void updateUserPrivacy(UserPrivacyUpdateRequest request, String identifier) throws Exception {
        Optional<User> user = userRepository.findById(identifier);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        user.get().setPrivateAccount(request.getPrivateAccount());
        userRepository.save(user.get());
    }

    public void updateProfilePhoto(MultipartFile file, String identifier) throws Exception {
        Optional<User> user = userRepository.findById(identifier);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        user.get().setProfilePhoto(file.getBytes());
        userRepository.save(user.get());
    }
}
