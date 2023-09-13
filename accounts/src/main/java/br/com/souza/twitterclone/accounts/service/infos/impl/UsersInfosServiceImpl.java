package br.com.souza.twitterclone.accounts.service.infos.impl;

import br.com.souza.twitterclone.accounts.configuration.security.TokenProvider;
import br.com.souza.twitterclone.accounts.database.model.Images;
import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.repository.IImagesRepository;
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
import java.util.UUID;
import javax.swing.text.html.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class UsersInfosServiceImpl implements IUsersInfosService {

    private final UserRepository userRepository;
    private final IImagesRepository iImagesRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final TokenProvider tokenProvider;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "twitterclone-new-register";
    private static final Integer PAUSE_TIME = 15000;
    private static final Integer LIMIT_TIME = 300;

    public UsersInfosServiceImpl(UserRepository userRepository,
                                 IImagesRepository iImagesRepository,
                                 PasswordEncoder passwordEncoder,
                                 RedisService redisService,
                                 TokenProvider tokenProvider,
                                 KafkaTemplate<String, String> kafkaTemplate) {
        this.userRepository = userRepository;
        this.iImagesRepository = iImagesRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.tokenProvider = tokenProvider;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void updateUserInfos(UserInfosUpdateRequest request, String identifier) throws Exception {
        User user = userRepository.findById(identifier)
                .orElseThrow(UserNotFoundException::new);

        user.setFirstName(request.getFirstName());
        user.setBiography(request.getBiography());
        user.setLocation(request.getLocation());
        user.setSite(request.getSite());

        userRepository.save(user);
    }

    @Override
    public void updateUserEmail(UserEmailUpdateRequest request, String identifier, String authorization) throws Exception {
        User user = userRepository.findById(identifier)
                .orElseThrow(UserNotFoundException::new);

        if (request.getEmail().equals(user.getEmail())) {
            return;
        }
        Optional<User> possibleUser = userRepository.findByEmail(request.getEmail());
        if (possibleUser.isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        user.setConfirmedEmail(false);
        user.setConfirmationCode(null);
        user.setEmail(request.getEmail());

        //remover autenticacao do usuário do Redis, obrigando-o a se logar novamente
        String sessionId = tokenProvider.getSessionIdentifierFromToken(authorization);
        redisService.removeKey(sessionId);
        redisService.removeKey(TokenProvider.AUTH + identifier);

        userRepository.save(user);

        trySendKafkaMessage(request.getEmail());
    }

    @Override
    public void updateUserUsername(UserUsernameUpdateRequest request, String identifier) throws Exception {
        User user = userRepository.findById(identifier)
                .orElseThrow(UserNotFoundException::new);

        if (request.getUsername().equals(user.getUsername())) {
            return;
        }
        if (UsernameValidatorHelper.isValidUsername(request.getUsername())) {
            throw new InvalidUsernameRegexException();
        }

        Optional<User> possibleUser = userRepository.findByUsername(request.getUsername());
        if (possibleUser.isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        user.setUsername(request.getUsername());
        userRepository.save(user);
    }

    @Override
    public void updateUserPassword(UserPasswordUpdateRequest request, String identifier, String authorization) throws Exception {
        User user = userRepository.findById(identifier)
                .orElseThrow(UserNotFoundException::new);

        if (!PasswordValidatorHelper.isValidPassword(request.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        if (!passwordEncoder.matches(request.getActualPassword(), user.getPassword())) {
            throw new ActualPasswordNotMatchesException();
        }
        if (!request.getNewPassword().equals(request.getNewPasswordConfirmation())) {
            throw new NewPasswordConfirmationNotMatchesNullException();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        //remover autenticacao do usuário do Redis, obrigando-o a se logar novamente
        String sessionId = tokenProvider.getSessionIdentifierFromToken(authorization);
        redisService.removeKey(sessionId);
        redisService.removeKey(TokenProvider.AUTH + identifier);

        userRepository.save(user);
    }

    @Override
    public void updateUserPrivacy(UserPrivacyUpdateRequest request, String identifier) throws Exception {
        User user = userRepository.findById(identifier)
                .orElseThrow(UserNotFoundException::new);

        user.setPrivateAccount(request.getPrivateAccount());
        userRepository.save(user);
    }

    @Override
    public void updateProfilePhoto(MultipartFile file, String userIdentifier) throws Exception {
        User user = userRepository.findById(userIdentifier)
                .orElseThrow(UserNotFoundException::new);

        if (user.getProfilePhotoIdentifier() != null) {
            Images profilePhoto = iImagesRepository.findById(user.getProfilePhotoIdentifier())
                    .orElseThrow(() -> new Exception("Image could not be found"));

            profilePhoto.setPhoto(!file.isEmpty() ? file.getBytes() : null);
            //profilePhoto.setXPosition(xPosition);
            //profilePhoto.setYPosition(yPosition);

            iImagesRepository.save(profilePhoto);
        } else {
            Images profilePhoto = iImagesRepository.save(Images.builder()
                    .identifier(UUID.randomUUID().toString())
                    .photo(!file.isEmpty() ? file.getBytes() : null)
                    //.xPosition(xPosition)
                    //.yPosition(yPosition)
                    .build());

            user.setProfilePhotoIdentifier(profilePhoto.getIdentifier());
            userRepository.save(user);
        }
    }

    @Override
    public void updateBackgroundPhoto(MultipartFile file, String identifier, Integer xPosition, Integer yPosition) throws Exception {
        User user = userRepository.findById(identifier)
                .orElseThrow(UserNotFoundException::new);

        if (user.getBackgroundPhotoIdentifier() != null) {
            Images backgroundPhoto = iImagesRepository.findById(user.getBackgroundPhotoIdentifier())
                    .orElseThrow(() -> new Exception("Image could not be found"));

            backgroundPhoto.setPhoto(!file.isEmpty() ? file.getBytes() : null);
            backgroundPhoto.setXPosition(xPosition);
            backgroundPhoto.setYPosition(yPosition);

            iImagesRepository.save(backgroundPhoto);
        } else {
            Images backgroundPhoto = iImagesRepository.save(Images.builder()
                    .identifier(UUID.randomUUID().toString())
                    .photo(!file.isEmpty() ? file.getBytes() : null)
                    .xPosition(xPosition)
                    .yPosition(yPosition)
                    .build());

            user.setBackgroundPhotoIdentifier(backgroundPhoto.getIdentifier());
            userRepository.save(user);
        }
    }

    @Override
    public void updateFirstAccessFlag(String identifier) throws Exception {
        User user = userRepository.findById(identifier)
                .orElseThrow(UserNotFoundException::new);

        user.setFirstAccess(false);
        userRepository.save(user);
    }

    private void trySendKafkaMessage(String email) throws Exception {
        boolean notSent = true;
        int waitingTime = 0;

        do {
            try {
                kafkaTemplate.send(TOPIC, email);
                notSent = false;
                log.error("Mensagem enviada com SUCESSO para o tópico: {}", TOPIC);
            } catch (Exception e) {
                log.error("Erro ao enviar mensagem para o tópico: {}", TOPIC);
                Thread.sleep(PAUSE_TIME);
                waitingTime += 15;
            }
        } while (notSent && waitingTime <= LIMIT_TIME);
    }
}
