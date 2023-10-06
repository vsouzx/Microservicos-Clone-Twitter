package br.com.souza.twitterclone.accounts.service.search.impl;

import br.com.souza.twitterclone.accounts.database.model.BlockedUsersId;
import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.repository.AlertedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.BlockedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.database.repository.impl.UsersRepositoryImpl;
import br.com.souza.twitterclone.accounts.database.repository.impl.WhoToFollowRepositoryImpl;
import br.com.souza.twitterclone.accounts.dto.user.FollowsAndFollowersResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import br.com.souza.twitterclone.accounts.dto.user.ValidEmailResponse;
import br.com.souza.twitterclone.accounts.dto.user.ValidUserResponse;
import br.com.souza.twitterclone.accounts.dto.user.ValidUsernameResponse;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.accounts.service.interactions.IUsersInteractionsService;
import br.com.souza.twitterclone.accounts.service.search.IUsersSearchService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UsersSearchServiceImpl implements IUsersSearchService {

    private final UserRepository userRepository;
    private final UsersRepositoryImpl usersRepositoryImpl;
    private final BlockedUsersRepository blockedUsersRepository;
    private final IUsersInteractionsService iUsersInteractionsService;
    private final AlertedUsersRepository alertedUsersRepository;
    private final WhoToFollowRepositoryImpl whoToFollowRepository;

    public UsersSearchServiceImpl(UserRepository userRepository,
                                  UsersRepositoryImpl usersRepositoryImpl,
                                  BlockedUsersRepository blockedUsersRepository,
                                  IUsersInteractionsService iUsersInteractionsService,
                                  AlertedUsersRepository alertedUsersRepository,
                                  WhoToFollowRepositoryImpl whoToFollowRepository) {
        this.userRepository = userRepository;
        this.usersRepositoryImpl = usersRepositoryImpl;
        this.blockedUsersRepository = blockedUsersRepository;
        this.iUsersInteractionsService = iUsersInteractionsService;
        this.alertedUsersRepository = alertedUsersRepository;
        this.whoToFollowRepository = whoToFollowRepository;
    }

    @Override
    public UserDetailsResponse searchUserInfos(String sessionUserIdentifier, String authorization) throws Exception {
        User user = userRepository.findById(sessionUserIdentifier)
                .orElseThrow(UserNotFoundException::new);

        return UserDetailsResponse.builder()
                .firstName(user.getFirstName())
                .username(user.getUsername())
                .following(iUsersInteractionsService.getUserFollowsCount(user.getIdentifier()))
                .followers(iUsersInteractionsService.getUserFollowersCount(user.getIdentifier()))
                .biography(user.getBiography())
                .location(user.getLocation())
                .site(user.getSite())
                .registrationTime(user.getRegistrationTime())
                .privateAccount(user.getPrivateAccount())
                .languagePreference(user.getLanguagePreference())
                .profilePhotoUrl(user.getProfilePhotoUrl())
                .backgroundPhotoUrl(user.getBackgroundPhotoUrl())
                .tweetsCount(iUsersInteractionsService.getTweetsCount(user.getIdentifier(), authorization))
                .build();
    }

    @Override
    public UserDetailsByIdentifierResponse searchUserInfosByIdentifier(String sessionUserIdentifier, String targetUserIdentifier, String authorization) throws Exception {
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
            return responseSessionUserIdentifierBlocked(targetUser, targetUserIdentifierBlocked, authorization);
        }
        if (targetUserIdentifierBlocked) {
            return responseTargetUserIdentifierBlocked(targetUser, authorization);
        }

        return fullResponse(targetUser, sessionUserIdentifier, authorization);
    }

    @Override
    public List<UserPreviewResponse> getUsersByUsername(String sessionUserIdentifier, String targetUsername, Integer page, Integer size) throws Exception {
        return usersRepositoryImpl.findAllByUsername(sessionUserIdentifier, targetUsername, page <= 0 ? 1 : page, size <= 0 ? 50 : size);
    }

    @Override
    public List<UserPreviewResponse> getUserFollowers(String sessionUserIdentifier, String targetUserIdentifier, Integer page, Integer size) throws Exception {
        User user = findUserByUsernameOrEmailOrIdentifier(targetUserIdentifier);
        return usersRepositoryImpl.getFollowers(sessionUserIdentifier, user.getIdentifier(), page, size);
    }

    @Override
    public List<UserPreviewResponse> getUserFollows(String sessionUserIdentifier, String targetUserIdentifier, Integer page, Integer size) throws Exception {
        User user = findUserByUsernameOrEmailOrIdentifier(targetUserIdentifier);
        return usersRepositoryImpl.getUserFollows(sessionUserIdentifier, user.getIdentifier(), page, size);
    }

    @Override
    public List<UserPreviewResponse> getUserPendingFollowers(String sessionUserIdentifier, Integer page, Integer size) throws Exception {
        return usersRepositoryImpl.getUserPendingFollowers(sessionUserIdentifier, page, size);
    }

    @Override
    public ValidEmailResponse isValidEmail(String email){
        return ValidEmailResponse.builder()
                .isValidEmail(userRepository.findByEmail(email).isEmpty())
                .build();
    }

    @Override
    public ValidUsernameResponse isValidUsername(String username){
        return ValidUsernameResponse.builder()
                .isValidUsername(userRepository.findByUsername(username).isEmpty())
                .build();
    }

    @Override
    public ValidUserResponse isValidUser(String username){
        Optional<User> user;

        user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return ValidUserResponse.builder()
                    .isValidUser(true)
                    .isUsername(true)
                    .isEmail(false)
                    .build();
        }

        user = userRepository.findByEmail(username);
        if(user.isPresent()){
            return ValidUserResponse.builder()
                    .isValidUser(true)
                    .isUsername(false)
                    .isEmail(true)
                    .build();
        }

        return ValidUserResponse.builder()
                .isValidUser(false)
                .build();
    }

    @Override
    public List<UserDetailsByIdentifierResponse> getWhoToFollow(String sessionUserIdentifier, Integer page, Integer size, String userOnScreen, String authorization) throws Exception {
        return whoToFollowRepository.find(sessionUserIdentifier, page, size, userOnScreen, authorization);
    }

    @Override
    public List<UserPreviewResponse> getVerified() {
        return userRepository.findAllByVerified(true).stream()
                .map(u -> {
                    try {
                        return new UserPreviewResponse(u);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAlertedUsers(String sessionUserIdentifier){
        return alertedUsersRepository.findAllByIdAlertedIdentifier(sessionUserIdentifier).stream()
                .map(user -> user.getId().getAlerterIdentifier())
                .toList();
    }

    @Override
    public List<UserPreviewResponse> getCommonFollows(String sessionUserIdentifier, String targetUserIdentifier) throws Exception {
        return iUsersInteractionsService.getCommonFollowers(sessionUserIdentifier, targetUserIdentifier);
    }

    @Override
    public FollowsAndFollowersResponse getFollowsAndFollowers(String targetUserIdentifier){
        return FollowsAndFollowersResponse.builder()
                .followers(iUsersInteractionsService.getUserFollowersCount(targetUserIdentifier))
                .follows(iUsersInteractionsService.getUserFollowsCount(targetUserIdentifier))
                .build();
    }

    private UserDetailsByIdentifierResponse responseSessionUserIdentifierBlocked(User targetUser, boolean isBlockedByMe, String authorization) throws Exception {
        return UserDetailsByIdentifierResponse.builder()
                .userIdentifier(targetUser.getIdentifier())
                .firstName(targetUser.getFirstName())
                .username(targetUser.getUsername())
                .following(iUsersInteractionsService.getUserFollowsCount(targetUser.getIdentifier()))
                .followers(iUsersInteractionsService.getUserFollowersCount(targetUser.getIdentifier()))
                .biography(null)
                .location(null)
                .site(null)
                .registrationTime(null)
                .privateAccount(targetUser.getPrivateAccount())
                .isBlockedByMe(isBlockedByMe)
                .hasBlockedMe(true)
                .isFollowedByMe(false)
                .isPendingFollowedByMe(false)
                .isFollowingMe(false)
                .isSilencedByMe(false)
                .profilePhotoUrl(targetUser.getProfilePhotoUrl())
                .backgroundPhotoUrl(targetUser.getBackgroundPhotoUrl())
                .tweetsCount(iUsersInteractionsService.getTweetsCount(targetUser.getIdentifier(), authorization))
                .build();
    }

    private UserDetailsByIdentifierResponse responseTargetUserIdentifierBlocked(User targetUser, String authorization) throws Exception {
        return UserDetailsByIdentifierResponse.builder()
                .userIdentifier(targetUser.getIdentifier())
                .firstName(targetUser.getFirstName())
                .username(targetUser.getUsername())
                .following(iUsersInteractionsService.getUserFollowsCount(targetUser.getIdentifier()))
                .followers(iUsersInteractionsService.getUserFollowersCount(targetUser.getIdentifier()))
                .biography(null)
                .location(null)
                .site(null)
                .registrationTime(null)
                .privateAccount(targetUser.getPrivateAccount())
                .isBlockedByMe(true)
                .hasBlockedMe(false)
                .isFollowedByMe(false)
                .isPendingFollowedByMe(false)
                .isFollowingMe(false)
                .isSilencedByMe(false)
                .profilePhotoUrl(targetUser.getProfilePhotoUrl())
                .backgroundPhotoUrl(targetUser.getBackgroundPhotoUrl())
                .tweetsCount(iUsersInteractionsService.getTweetsCount(targetUser.getIdentifier(), authorization))
                .build();
    }

    private UserDetailsByIdentifierResponse fullResponse(User targetUser, String sessionUser, String authorization) throws Exception {
        return UserDetailsByIdentifierResponse.builder()
                .userIdentifier(targetUser.getIdentifier())
                .firstName(targetUser.getFirstName())
                .username(targetUser.getUsername())
                .following(iUsersInteractionsService.getUserFollowsCount(targetUser.getIdentifier()))
                .followers(iUsersInteractionsService.getUserFollowersCount(targetUser.getIdentifier()))
                .biography(targetUser.getBiography())
                .location(targetUser.getLocation())
                .site(targetUser.getSite())
                .registrationTime(targetUser.getRegistrationTime())
                .privateAccount(targetUser.getPrivateAccount())
                .isBlockedByMe(false)
                .hasBlockedMe(false)
                .isFollowedByMe(iUsersInteractionsService.verifyIfIsFollowing(sessionUser, targetUser.getIdentifier()).isPresent())
                .isPendingFollowedByMe(iUsersInteractionsService.verifyIfIsPendingFollowing(sessionUser, targetUser.getIdentifier()).isPresent())
                .isFollowingMe(iUsersInteractionsService.verifyIfIsFollowing(targetUser.getIdentifier(), sessionUser).isPresent())
                .isSilencedByMe(iUsersInteractionsService.verifyIfIsSilenced(sessionUser, targetUser.getIdentifier()).isPresent())
                .isNotificationsAlertedByMe(iUsersInteractionsService.verifyIfIsAlerted(sessionUser, targetUser.getIdentifier()).isPresent())
                .profilePhotoUrl(targetUser.getProfilePhotoUrl())
                .backgroundPhotoUrl(targetUser.getBackgroundPhotoUrl())
                .tweetsCount(iUsersInteractionsService.getTweetsCount(targetUser.getIdentifier(), authorization))
                .isVerified(targetUser.getVerified())
                .build();
    }

    private User findUserByUsernameOrEmailOrIdentifier(String targetUserIdentifier) throws UserNotFoundException {
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
