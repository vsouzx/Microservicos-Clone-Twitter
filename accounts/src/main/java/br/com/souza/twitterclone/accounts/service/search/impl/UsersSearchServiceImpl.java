package br.com.souza.twitterclone.accounts.service.search.impl;

import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.accounts.service.search.IUsersSearchService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsersSearchServiceImpl implements IUsersSearchService {

    private final UserRepository userRepository;

    public UsersSearchServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetailsResponse searchUserInfos(String userIdentifier) throws Exception { //TODO: Separar para um novo service
        Optional<User> possibleUser = userRepository.findById(userIdentifier);

        if (possibleUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        return UserDetailsResponse.builder()
                .firstName(possibleUser.get().getFirstName())
                .lastName(possibleUser.get().getLastName())
                .username(possibleUser.get().getUsername())
                .email(possibleUser.get().getEmail())
                .biography(possibleUser.get().getBiography())
                .location(possibleUser.get().getLocation())
                .site(possibleUser.get().getSite())
                .registrationTime(possibleUser.get().getRegistrationTime())
                .privateAccount(possibleUser.get().getPrivateAccount())
                .build();
    }
}
