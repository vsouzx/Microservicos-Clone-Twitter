package br.com.souza.twitterclone.accounts.service.search.impl;

import br.com.souza.twitterclone.accounts.database.model.BlockedUsersId;
import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.repository.BlockedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.database.repository.impl.UsersRepositoryImpl;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.accounts.service.interactions.IUsersInteractionsService;
import br.com.souza.twitterclone.accounts.service.search.IUsersSearchService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UsersSearchServiceImpl implements IUsersSearchService {

    private final UserRepository userRepository;
    private final UsersRepositoryImpl usersRepositoryImpl;
    private final BlockedUsersRepository blockedUsersRepository;
    private final IUsersInteractionsService iUsersInteractionsService;

    public UsersSearchServiceImpl(UserRepository userRepository,
                                  UsersRepositoryImpl usersRepositoryImpl,
                                  BlockedUsersRepository blockedUsersRepository,
                                  IUsersInteractionsService iUsersInteractionsService) {
        this.userRepository = userRepository;
        this.usersRepositoryImpl = usersRepositoryImpl;
        this.blockedUsersRepository = blockedUsersRepository;
        this.iUsersInteractionsService = iUsersInteractionsService;
    }

    @Override
    public UserDetailsResponse searchUserInfos(String sessionUserIdentifier) throws Exception {
        User possibleUser = userRepository.findById(sessionUserIdentifier)
                .orElseThrow(UserNotFoundException::new);

        return UserDetailsResponse.builder()
                .firstName(possibleUser.getFirstName())
                .username(possibleUser.getUsername())
                .following(iUsersInteractionsService.getUserFollowsCount(sessionUserIdentifier, possibleUser.getIdentifier()))
                .followers(iUsersInteractionsService.getUserFollowersCount(sessionUserIdentifier, possibleUser.getIdentifier()))
                .biography(possibleUser.getBiography())
                .location(possibleUser.getLocation())
                .site(possibleUser.getSite())
                .registrationTime(possibleUser.getRegistrationTime())
                .privateAccount(possibleUser.getPrivateAccount())
                .languagePreference(possibleUser.getLanguagePreference())
                .profilePhoto(possibleUser.getProfilePhoto())
                .build();
    }

    @Override
    public UserDetailsByIdentifierResponse searchUserInfosByIdentifier(String sessionUserIdentifier, String targetUserIdentifier) throws Exception {
        User targetUser = userRepository.findById(targetUserIdentifier)
                .orElseThrow(UserNotFoundException::new);

        boolean isSessionUserIdentifierBlocked = blockedUsersRepository.findById(BlockedUsersId.builder()
                .blockerIdentifier(targetUserIdentifier)
                .blockedIdentifier(sessionUserIdentifier)
                .build()).isPresent();

        boolean targetUserIdentifierBlocked = blockedUsersRepository.findById(BlockedUsersId.builder()
                .blockerIdentifier(sessionUserIdentifier)
                .blockedIdentifier(targetUserIdentifier)
                .build()).isPresent();

        if (isSessionUserIdentifierBlocked) {
            return responseSessionUserIdentifierBlocked(sessionUserIdentifier, targetUser, targetUserIdentifierBlocked);
        }
        if (targetUserIdentifierBlocked) {
            return responseTargetUserIdentifierBlocked(sessionUserIdentifier, targetUser);
        }

        return fullResponse(targetUser, sessionUserIdentifier);
    }

    @Override
    public List<UserPreviewResponse> getUsersByUsername(String sessionUserIdentifier, String targetUsername, Integer page, Integer size) throws Exception {
        return usersRepositoryImpl.findAllByUsername(sessionUserIdentifier, targetUsername, page <= 0 ? 1 : page, size <= 0 ? 50 : size);
    }

    @Override
    public List<UserPreviewResponse> getUserFollowers(String sessionUserIdentifier, String targetUsername, Integer page, Integer size) throws Exception {
        return usersRepositoryImpl.getFollowers(sessionUserIdentifier, targetUsername, page, size);
    }

    @Override
    public List<UserPreviewResponse> getUserFollows(String sessionUserIdentifier, String targetUsername, Integer page, Integer size) throws Exception {
        return usersRepositoryImpl.getUserFollows(sessionUserIdentifier, targetUsername, page, size);
    }

    @Override
    public List<UserPreviewResponse> getUserPendingFollowers(String sessionUserIdentifier, Integer page, Integer size) throws Exception {
        return usersRepositoryImpl.getUserPendingFollowers(sessionUserIdentifier, page, size);
    }

    private UserDetailsByIdentifierResponse responseSessionUserIdentifierBlocked(String sessionUserIdentifier, User targetUser, boolean isBlockedByMe) {
        return UserDetailsByIdentifierResponse.builder()
                .firstName(targetUser.getFirstName())
                .username(targetUser.getUsername())
                .following(iUsersInteractionsService.getUserFollowsCount(sessionUserIdentifier, targetUser.getIdentifier()))
                .followers(iUsersInteractionsService.getUserFollowersCount(sessionUserIdentifier, targetUser.getIdentifier()))
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

    private UserDetailsByIdentifierResponse responseTargetUserIdentifierBlocked(String sessionUserIdentifier, User targetUser) {
        return UserDetailsByIdentifierResponse.builder()
                .firstName(targetUser.getFirstName())
                .username(targetUser.getUsername())
                .following(iUsersInteractionsService.getUserFollowsCount(sessionUserIdentifier, targetUser.getIdentifier()))
                .followers(iUsersInteractionsService.getUserFollowersCount(sessionUserIdentifier, targetUser.getIdentifier()))
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
                .username(targetUser.getUsername())
                .following(iUsersInteractionsService.getUserFollowsCount(sessionUser, targetUser.getIdentifier()))
                .followers(iUsersInteractionsService.getUserFollowersCount(sessionUser, targetUser.getIdentifier()))
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
