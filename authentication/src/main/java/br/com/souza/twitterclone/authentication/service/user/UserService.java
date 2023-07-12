package br.com.souza.twitterclone.authentication.service.user;

import br.com.souza.twitterclone.authentication.database.model.User;
import br.com.souza.twitterclone.authentication.database.repository.UserRepository;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> possibleUser = userRepository.findByUsername(username);

        if(possibleUser.isEmpty()){
            possibleUser = userRepository.findByEmail(username);
        }

        if(possibleUser.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        return possibleUser.get();
    }
}
