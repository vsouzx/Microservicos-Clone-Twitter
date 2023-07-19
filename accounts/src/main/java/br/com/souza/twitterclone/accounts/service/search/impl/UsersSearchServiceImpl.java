package br.com.souza.twitterclone.accounts.service.search.impl;

import br.com.souza.twitterclone.accounts.database.model.BlockedUsers;
import br.com.souza.twitterclone.accounts.database.model.BlockedUsersId;
import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.repository.BlockedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.handler.exceptions.BlockedUserException;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.accounts.service.search.IUsersSearchService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsersSearchServiceImpl implements IUsersSearchService {

    private final UserRepository userRepository;
    private final BlockedUsersRepository blockedUsersRepository;

    public UsersSearchServiceImpl(UserRepository userRepository,
                                  BlockedUsersRepository blockedUsersRepository) {
        this.userRepository = userRepository;
        this.blockedUsersRepository = blockedUsersRepository;
    }

    public UserDetailsResponse searchUserInfos(String userIdentifier) throws Exception {
        Optional<User> possibleUser = userRepository.findById(userIdentifier);

        if (possibleUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        return UserDetailsResponse.builder()
                .firstName(possibleUser.get().getFirstName())
                .lastName(possibleUser.get().getLastName())
                .username(possibleUser.get().getUsername())
                .follows(0)  //TODO: adicionar lógica para mostrar quantidade de followers
                .followers(0) //TODO: adicionar lógica para mostrar quantidade de followers
                .biography(possibleUser.get().getBiography())
                .location(possibleUser.get().getLocation())
                .site(possibleUser.get().getSite())
                .registrationTime(possibleUser.get().getRegistrationTime())
                .privateAccount(possibleUser.get().getPrivateAccount())
                .languagePreference(possibleUser.get().getLanguagePreference())
                .profilePhoto(possibleUser.get().getProfilePhoto())
                .build();
    }

    public UserDetailsByIdentifierResponse searchUserInfosByIdentifier(String sessionUserIdentifier, String targetUserIdentifier) throws Exception {
        Optional<User> targetUser = userRepository.findById(targetUserIdentifier);

        if (targetUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        boolean isSessionUserIdentifierBlocked = blockedUsersRepository.findById(BlockedUsersId.builder()
                .blockerIdentifier(targetUserIdentifier)
                .blockedIdentifier(sessionUserIdentifier)
                .build()).isPresent();

        boolean targetUserIdentifierBlocked = blockedUsersRepository.findById(BlockedUsersId.builder()
                .blockerIdentifier(sessionUserIdentifier)
                .blockedIdentifier(targetUserIdentifier)
                .build()).isPresent();

        if (isSessionUserIdentifierBlocked) {
            return responseSessionUserIdentifierBlocked(targetUser.get(), targetUserIdentifierBlocked);
        }
        if (targetUserIdentifierBlocked) {
            return responseTargetUserIdentifierBlocked(targetUser.get());
        }

        return fullResponse(targetUser.get());
    }

    private UserDetailsByIdentifierResponse responseSessionUserIdentifierBlocked(User targetUser, boolean isBlockedByMe) {
        return UserDetailsByIdentifierResponse.builder()
                .firstName(targetUser.getFirstName())
                .lastName(targetUser.getLastName())
                .username(targetUser.getUsername())
                .follows(0)  //TODO: adicionar lógica para mostrar quantidade de followers
                .followers(0) //TODO: adicionar lógica para mostrar quantidade de followers
                .biography(null)
                .location(null)
                .site(null)
                .registrationTime(null)
                .privateAccount(targetUser.getPrivateAccount())
                .isBlockedByMe(isBlockedByMe)
                .hasBlockedMe(true)
                .followersInCommon(null)
                .isFollowedByMe(false)
                .isFollowingMe(false)
                .profilePhoto(targetUser.getProfilePhoto())
                .build();
    }

    private UserDetailsByIdentifierResponse responseTargetUserIdentifierBlocked(User targetUser) {
        return UserDetailsByIdentifierResponse.builder()
                .firstName(targetUser.getFirstName())
                .lastName(targetUser.getLastName())
                .username(targetUser.getUsername())
                .follows(0)  //TODO: adicionar lógica para mostrar quantidade de followers
                .followers(0) //TODO: adicionar lógica para mostrar quantidade de followers
                .biography(null)
                .location(null)
                .site(null)
                .registrationTime(null)
                .privateAccount(targetUser.getPrivateAccount())
                .isBlockedByMe(true)
                .hasBlockedMe(false)
                .followersInCommon(null)
                .isFollowedByMe(false)
                .isFollowingMe(false)
                .profilePhoto(targetUser.getProfilePhoto())
                .build();
    }

    private UserDetailsByIdentifierResponse fullResponse(User targetUser) {
        return UserDetailsByIdentifierResponse.builder()
                .firstName(targetUser.getFirstName())
                .lastName(targetUser.getLastName())
                .username(targetUser.getUsername())
                .follows(0)   //TODO: adicionar lógica para mostrar quantidade de followers
                .followers(0) //TODO: adicionar lógica para mostrar quantidade de followers
                .biography(targetUser.getBiography())
                .location(targetUser.getLocation())
                .site(targetUser.getSite())
                .registrationTime(targetUser.getRegistrationTime())
                .privateAccount(targetUser.getPrivateAccount())
                .isBlockedByMe(false)
                .hasBlockedMe(false)
                .followersInCommon(null) //TODO: Adicionar lógica para retornar lista de seguidores em comum
                .isFollowedByMe(null) //TODO: Adicionar lógica para saber se segue ou não
                .isFollowingMe(null) //TODO: Adicionar lógica para saber se ta me seguindo ou não
                .profilePhoto(targetUser.getProfilePhoto())
                .build();
    }

}
