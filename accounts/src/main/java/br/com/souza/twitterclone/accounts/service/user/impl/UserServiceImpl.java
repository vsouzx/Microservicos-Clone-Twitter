package br.com.souza.twitterclone.accounts.service.user.impl;

import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.accounts.service.user.IUserService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserByUsernameOrEmailOrIdentifier(String targetUserIdentifier) throws UserNotFoundException {
        Optional<User> user;

        user = userRepository.findByUsername(targetUserIdentifier);
        if(user.isEmpty()){
            user = userRepository.findByEmail(targetUserIdentifier);
        }
        if(user.isEmpty()){
            user = userRepository.findById(targetUserIdentifier);
        }
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }
        return user.get();
    }
}
