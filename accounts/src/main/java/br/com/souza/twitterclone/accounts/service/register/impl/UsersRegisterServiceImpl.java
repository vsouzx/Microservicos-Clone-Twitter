package br.com.souza.twitterclone.accounts.service.register.impl;

import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.dto.user.UserRegistrationRequest;
import br.com.souza.twitterclone.accounts.handler.exceptions.ConfirmationCodeNotMatchesException;
import br.com.souza.twitterclone.accounts.handler.exceptions.EmailAlreadyConfirmedException;
import br.com.souza.twitterclone.accounts.handler.exceptions.EmailAlreadyExistsException;
import br.com.souza.twitterclone.accounts.handler.exceptions.InvalidPasswordException;
import br.com.souza.twitterclone.accounts.handler.exceptions.InvalidUsernameRegexException;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.accounts.handler.exceptions.UsernameAlreadyExistsException;
import br.com.souza.twitterclone.accounts.service.register.IUsersRegisterService;
import br.com.souza.twitterclone.accounts.util.PasswordValidatorHelper;
import br.com.souza.twitterclone.accounts.util.UsernameValidatorHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class UsersRegisterServiceImpl implements IUsersRegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "twitterclone-new-register";
    private static final Integer PAUSE_TIME = 15000;
    private static final Integer LIMIT_TIME = 300;

    public UsersRegisterServiceImpl(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    KafkaTemplate<String, String> kafkaTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void userRegister(String requestJson, MultipartFile file) throws Exception {
        UserRegistrationRequest request;

        try{
            ObjectMapper mapper = new ObjectMapper();
            request = mapper.readValue(requestJson, UserRegistrationRequest.class);
        }catch (Exception e){
            throw new Exception("Erro ao ler Json: " + e);
        }

        Optional<User> user = userRepository.findByUsername(request.getUsername());

        if (user.isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        user = userRepository.findByEmail(request.getEmail());

        if (user.isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        if(UsernameValidatorHelper.isValidUsername(request.getUsername())){
            throw new InvalidUsernameRegexException();
        }

        if(!PasswordValidatorHelper.isValidPassword(request.getPassword())){
            throw new InvalidPasswordException();
        }

        userRepository.save(User.builder()
                .identifier(UUID.randomUUID().toString())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .biography(request.getBiography())
                .location(request.getLocation())
                .site(request.getSite())
                .confirmedEmail(false) //TODO: Alterar para false depois que fazer o servico de validação email
                .registrationTime(LocalDateTime.now())
                .privateAccount(request.getPrivateAccount())
                .languagePreference(request.getLanguagePreference() == null ? "pt" : request.getLanguagePreference())
                .profilePhoto(file.getBytes())
                .build());

        trySendKafkaMessage(request.getEmail());
    }

    @Override
    public void resendConfirmationCode(String email) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        if(user.get().getConfirmedEmail()){
            throw new EmailAlreadyConfirmedException();
        }

        trySendKafkaMessage(email);
    }

    @Override
    public void confirmCode(String email, String code) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        if(user.get().getConfirmedEmail()){
            throw new EmailAlreadyConfirmedException();
        }

        if(!user.get().getConfirmationCode().equals(code)){
            throw new ConfirmationCodeNotMatchesException();
        }

        user.get().setConfirmedEmail(true);
        userRepository.save(user.get());
    }

    private void trySendKafkaMessage(String email) throws Exception {
        boolean notSent = true;
        int waitingTime = 0;

        do{
            try{
                kafkaTemplate.send(TOPIC, email);
                notSent = false;
                log.error("Mensagem enviada com SUCESSO para o tópico: {}", TOPIC);
            }catch (Exception e){
                log.error("Erro ao enviar mensagem para o tópico: {}", TOPIC);
                Thread.sleep(PAUSE_TIME);
                waitingTime += 15;
            }
        }while (notSent && waitingTime <= LIMIT_TIME);
    }

}
