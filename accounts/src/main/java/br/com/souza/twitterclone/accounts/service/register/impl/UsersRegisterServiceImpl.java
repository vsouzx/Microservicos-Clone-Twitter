package br.com.souza.twitterclone.accounts.service.register.impl;

import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.dto.user.UserConfirmationCodeResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserRegistrationRequest;
import br.com.souza.twitterclone.accounts.handler.exceptions.ConfirmationCodeNotMatchesException;
import br.com.souza.twitterclone.accounts.handler.exceptions.EmailAlreadyExistsException;
import br.com.souza.twitterclone.accounts.handler.exceptions.InvalidPasswordException;
import br.com.souza.twitterclone.accounts.service.redis.RedisService;
import br.com.souza.twitterclone.accounts.service.register.IUsersRegisterService;
import br.com.souza.twitterclone.accounts.util.PasswordValidatorHelper;
import br.com.souza.twitterclone.accounts.util.RandomNumberUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UsersRegisterServiceImpl implements IUsersRegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "twitterclone-new-register";
    private static final Integer PAUSE_TIME = 15000;
    private static final Integer LIMIT_TIME = 300;

    public UsersRegisterServiceImpl(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    RedisService redisService,
                                    KafkaTemplate<String, String> kafkaTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void userRegister(UserRegistrationRequest request) throws Exception {
        String username;
        Optional<User> user;
        boolean isValid = false;
        do{
            username = request.getFirstName().replace(" ", "") + RandomNumberUtil.generateRandomCode();

            user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                isValid = true;
            }
        }while(!isValid);

        user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        if (!PasswordValidatorHelper.isValidPassword(request.getPassword())) {
            throw new InvalidPasswordException();
        }

        UserConfirmationCodeResponse confirmationCodeResponse = (UserConfirmationCodeResponse) redisService.getValue(request.getEmail(), UserConfirmationCodeResponse.class);
        if(confirmationCodeResponse == null || !confirmationCodeResponse.getConfirmationCode().equals(request.getConfirmationCode())){
            throw new ConfirmationCodeNotMatchesException();
        }

        userRepository.save(User.builder()
                .identifier(UUID.randomUUID().toString())
                .firstName(request.getFirstName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(username)
                .registrationTime(LocalDateTime.now())
                .privateAccount(false)
                .confirmedEmail(true)
                .languagePreference("pt")
                .birthDate(request.getBirthDate())
                .firstAccess(true)
                .build());

        redisService.removeKey(request.getEmail());
    }

    @Override
    public void sendConfirmationCode(String email) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            throw new EmailAlreadyExistsException();
        }

        UserConfirmationCodeResponse confirmationCodeResponse = (UserConfirmationCodeResponse) redisService.getValue(email, UserConfirmationCodeResponse.class);
        if(confirmationCodeResponse == null){
            confirmationCodeResponse = UserConfirmationCodeResponse.builder()
                    .email(email)
                    .confirmationCode(RandomNumberUtil.generateRandomCode())
                    .build();

            redisService.setValue(email, confirmationCodeResponse, TimeUnit.MILLISECONDS, 1800000L, true);
        }

        trySendKafkaMessage(confirmationCodeResponse.toString());
    }

    @Override
    public void confirmCode(String email, String code) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            throw new EmailAlreadyExistsException();
        }

        UserConfirmationCodeResponse confirmationCodeResponse = (UserConfirmationCodeResponse) redisService.getValue(email, UserConfirmationCodeResponse.class);
        if (!confirmationCodeResponse.getConfirmationCode().equals(code)) {
            throw new ConfirmationCodeNotMatchesException();
        }
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
