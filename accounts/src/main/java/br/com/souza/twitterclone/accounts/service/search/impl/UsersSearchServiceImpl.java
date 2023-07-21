package br.com.souza.twitterclone.accounts.service.search.impl;

import br.com.souza.twitterclone.accounts.database.model.BlockedUsersId;
import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.repository.BlockedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.accounts.service.interactions.IUsersInteractionsService;
import br.com.souza.twitterclone.accounts.service.search.IUsersSearchService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsersSearchServiceImpl implements IUsersSearchService {

    private final UserRepository userRepository;
    private final BlockedUsersRepository blockedUsersRepository;
    private final IUsersInteractionsService iUsersInteractionsService;

    public UsersSearchServiceImpl(UserRepository userRepository,
                                  BlockedUsersRepository blockedUsersRepository,
                                  IUsersInteractionsService iUsersInteractionsService){
        this.userRepository = userRepository;
        this.blockedUsersRepository = blockedUsersRepository;
        this.iUsersInteractionsService = iUsersInteractionsService;
    }

    @Override
    public UserDetailsResponse searchUserInfos(String sessionUserIdentifier) throws Exception {
        Optional<User> possibleUser = userRepository.findById(sessionUserIdentifier);

        if (possibleUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        return UserDetailsResponse.builder()
                .firstName(possibleUser.get().getFirstName())
                .lastName(possibleUser.get().getLastName())
                .username(possibleUser.get().getUsername())
                .following(iUsersInteractionsService.getUserFollows(possibleUser.get().getIdentifier()).size())
                .followers(iUsersInteractionsService.getUserFollowers(possibleUser.get().getIdentifier()).size())
                .biography(possibleUser.get().getBiography())
                .location(possibleUser.get().getLocation())
                .site(possibleUser.get().getSite())
                .registrationTime(possibleUser.get().getRegistrationTime())
                .privateAccount(possibleUser.get().getPrivateAccount())
                .languagePreference(possibleUser.get().getLanguagePreference())
                .profilePhoto(possibleUser.get().getProfilePhoto())
                .build();
    }

    @Override
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

    @Override
    public List<UserPreviewResponse> getUsersByUsername(String userIdentifier, String targetUserIdentifier) throws Exception {
        return null;
    }

    private UserDetailsByIdentifierResponse responseSessionUserIdentifierBlocked(User targetUser, boolean isBlockedByMe) {
        return UserDetailsByIdentifierResponse.builder()
                .firstName(targetUser.getFirstName())
                .lastName(targetUser.getLastName())
                .username(targetUser.getUsername())
                .following(iUsersInteractionsService.getUserFollows(targetUser.getIdentifier()).size())
                .followers(iUsersInteractionsService.getUserFollowers(targetUser.getIdentifier()).size())
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
                .following(iUsersInteractionsService.getUserFollows(targetUser.getIdentifier()).size())
                .followers(iUsersInteractionsService.getUserFollowers(targetUser.getIdentifier()).size())
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
                .following(iUsersInteractionsService.getUserFollows(targetUser.getIdentifier()).size())
                .followers(iUsersInteractionsService.getUserFollowers(targetUser.getIdentifier()).size())
                .biography(targetUser.getBiography())
                .location(targetUser.getLocation())
                .site(targetUser.getSite())
                .registrationTime(targetUser.getRegistrationTime())
                .privateAccount(targetUser.getPrivateAccount())
                .isBlockedByMe(false)
                .hasBlockedMe(false)
                .followersInCommon(iUsersInteractionsService.getCommonFollowers(sessionUser, targetUser.getIdentifier()))
                .isFollowedByMe(iUsersInteractionsService.verifyIfIsFollowing(sessionUser, targetUser.getIdentifier()).isPresent())
                .isPendingFollowedByMe(iUsersInteractionsService.verifyIfIsPendingFollowing(sessionUser, targetUser.getIdentifier()).isPresent())
                .isFollowingMe(iUsersInteractionsService.verifyIfIsFollowing(targetUser.getIdentifier(), sessionUser).isPresent())
                .isSilencedByMe(iUsersInteractionsService.verifyIfIsSilenced(sessionUser, targetUser.getIdentifier()).isPresent())
                .profilePhoto(targetUser.getProfilePhoto())
                .build();
    }

}
