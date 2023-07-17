package br.com.souza.twitterclone.accounts.service.register.impl;

import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.dto.user.UserRegistrationRequest;
import br.com.souza.twitterclone.accounts.handler.exceptions.EmailAlreadyExistsException;
import br.com.souza.twitterclone.accounts.handler.exceptions.InvalidUsernameRegexException;
import br.com.souza.twitterclone.accounts.handler.exceptions.UsernameAlreadyExistsException;
import br.com.souza.twitterclone.accounts.service.register.IUsersRegisterService;
import br.com.souza.twitterclone.accounts.util.UsernameValidatorHelper;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersRegisterServiceImpl implements IUsersRegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersRegisterServiceImpl(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void userRegister(UserRegistrationRequest request) throws Exception {
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
                .confirmedEmail(true) //TODO: Alterar para false depois que fazer o servico de validação email
                .registrationTime(LocalDateTime.now())
                .followers(0)
                .following(0)
                .privateAccount(request.getPrivateAccount())
                .build());
    }
}
