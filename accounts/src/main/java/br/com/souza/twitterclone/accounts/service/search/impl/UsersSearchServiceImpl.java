package br.com.souza.twitterclone.accounts.service.search.impl;

import br.com.souza.twitterclone.accounts.database.model.BlockedUsersId;
import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.model.UsersFollowsId;
import br.com.souza.twitterclone.accounts.database.model.UsersPendingFollowsId;
import br.com.souza.twitterclone.accounts.database.repository.BlockedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.database.repository.UsersFollowsRepository;
import br.com.souza.twitterclone.accounts.database.repository.UsersPendingFollowsRepository;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.accounts.service.search.IUsersSearchService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsersSearchServiceImpl implements IUsersSearchService {

    private final UserRepository userRepository;
    private final BlockedUsersRepository blockedUsersRepository;
    private final UsersFollowsRepository usersFollowsRepository;
    private final UsersPendingFollowsRepository usersPendingFollowsRepository;

    public UsersSearchServiceImpl(UserRepository userRepository,
                                  BlockedUsersRepository blockedUsersRepository,
                                  UsersFollowsRepository usersFollowsRepository,
                                  UsersPendingFollowsRepository usersPendingFollowsRepository) {
        this.userRepository = userRepository;
        this.blockedUsersRepository = blockedUsersRepository;
        this.usersFollowsRepository = usersFollowsRepository;
        this.usersPendingFollowsRepository = usersPendingFollowsRepository;
    }

    public UserDetailsResponse searchUserInfos(String sessionUserIdentifier) throws Exception {
        Optional<User> possibleUser = userRepository.findById(sessionUserIdentifier);

        if (possibleUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        return UserDetailsResponse.builder()
                .firstName(possibleUser.get().getFirstName())
                .lastName(possibleUser.get().getLastName())
                .username(possibleUser.get().getUsername())
                .following(getUserFollows(possibleUser.get().getIdentifier()))
                .followers(getUserFollowers(possibleUser.get().getIdentifier()))
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

        return fullResponse(targetUser.get(), sessionUserIdentifier);
    }

    private UserDetailsByIdentifierResponse responseSessionUserIdentifierBlocked(User targetUser, boolean isBlockedByMe) {
        return UserDetailsByIdentifierResponse.builder()
                .firstName(targetUser.getFirstName())
                .lastName(targetUser.getLastName())
                .username(targetUser.getUsername())
                .following(getUserFollows(targetUser.getIdentifier()))
                .followers(getUserFollowers(targetUser.getIdentifier()))
                .biography(null)
                .location(null)
                .site(null)
                .registrationTime(null)
                .privateAccount(targetUser.getPrivateAccount())
                .isBlockedByMe(isBlockedByMe)
                .hasBlockedMe(true)
                .followersInCommon(null)
                .isFollowedByMe(false)
                .isPendingFollowedByMe(false)
                .isFollowingMe(false)
                .isSilencedByMe(false)
                .profilePhoto(targetUser.getProfilePhoto())
                .build();
    }

    private UserDetailsByIdentifierResponse responseTargetUserIdentifierBlocked(User targetUser) {
        return UserDetailsByIdentifierResponse.builder()
                .firstName(targetUser.getFirstName())
                .lastName(targetUser.getLastName())
                .username(targetUser.getUsername())
                .following(getUserFollows(targetUser.getIdentifier()))
                .followers(getUserFollowers(targetUser.getIdentifier()))
                .biography(null)
                .location(null)
                .site(null)
                .registrationTime(null)
                .privateAccount(targetUser.getPrivateAccount())
                .isBlockedByMe(true)
                .hasBlockedMe(false)
                .followersInCommon(null)
                .isFollowedByMe(false)
                .isPendingFollowedByMe(false)
                .isFollowingMe(false)
                .isSilencedByMe(false)
                .profilePhoto(targetUser.getProfilePhoto())
                .build();
    }

    private UserDetailsByIdentifierResponse fullResponse(User targetUser, String sessionUser) {
        return UserDetailsByIdentifierResponse.builder()
                .firstName(targetUser.getFirstName())
                .lastName(targetUser.getLastName())
                .username(targetUser.getUsername())
                .following(getUserFollows(targetUser.getIdentifier()))
                .followers(getUserFollowers(targetUser.getIdentifier()))
                .biography(targetUser.getBiography())
                .location(targetUser.getLocation())
                .site(targetUser.getSite())
                .registrationTime(targetUser.getRegistrationTime())
                .privateAccount(targetUser.getPrivateAccount())
                .isBlockedByMe(false)
                .hasBlockedMe(false)
                .followersInCommon(null) //TODO: Adicionar lógica para retornar lista de seguidores em comum
                .isFollowedByMe(isFollowing(sessionUser, targetUser.getIdentifier()))
                .isPendingFollowedByMe(isPendingFollowing(sessionUser, targetUser.getIdentifier()))
                .isFollowingMe(isFollowing(targetUser.getIdentifier(), sessionUser))
                .isSilencedByMe(null)
                .profilePhoto(targetUser.getProfilePhoto())
                .build();
    }

    private Integer getUserFollowers(String user){
        return usersFollowsRepository.findAllByIdFollowedIdentifier(user).size();
    }

    private Integer getUserFollows(String user){
        return usersFollowsRepository.findAllByIdFollowerIdentifier(user).size();
    }

    private boolean isFollowing(String sessionUser, String targetUser){
        return usersFollowsRepository.findById(UsersFollowsId.builder()
                        .followerIdentifier(sessionUser)
                        .followedIdentifier(targetUser)
                .build()).isPresent();
    }

    private boolean isPendingFollowing(String sessionUser, String targetUser){
        return usersPendingFollowsRepository.findById(UsersPendingFollowsId.builder()
                .pendingFollowerIdentifier(sessionUser)
                .pendingFollowedIdentifier(targetUser)
                .build()).isPresent();
    }

}
