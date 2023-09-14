package br.com.souza.twitterclone.accounts.service.search.impl;

import br.com.souza.twitterclone.accounts.database.model.BlockedUsersId;
import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.repository.BlockedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.IImagesRepository;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.database.repository.impl.UsersRepositoryImpl;
import br.com.souza.twitterclone.accounts.dto.user.*;
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
    private final IImagesRepository iImagesRepository;

    public UsersSearchServiceImpl(UserRepository userRepository,
                                  UsersRepositoryImpl usersRepositoryImpl,
                                  BlockedUsersRepository blockedUsersRepository,
                                  IUsersInteractionsService iUsersInteractionsService,
                                  IImagesRepository iImagesRepository) {
        this.userRepository = userRepository;
        this.usersRepositoryImpl = usersRepositoryImpl;
        this.blockedUsersRepository = blockedUsersRepository;
        this.iUsersInteractionsService = iUsersInteractionsService;
        this.iImagesRepository = iImagesRepository;
    }

    @Override
    public UserDetailsResponse searchUserInfos(String sessionUserIdentifier) throws Exception {
        User user = userRepository.findById(sessionUserIdentifier)
                .orElseThrow(UserNotFoundException::new);

        return UserDetailsResponse.builder()
                .firstName(user.getFirstName())
                .username(user.getUsername())
                .following(iUsersInteractionsService.getUserFollowsCount(sessionUserIdentifier, user.getIdentifier()))
                .followers(iUsersInteractionsService.getUserFollowersCount(sessionUserIdentifier, user.getIdentifier()))
                .biography(user.getBiography())
                .location(user.getLocation())
                .site(user.getSite())
                .registrationTime(user.getRegistrationTime())
                .privateAccount(user.getPrivateAccount())
                .languagePreference(user.getLanguagePreference())
                .profilePhoto(user.getProfilePhotoIdentifier() != null ? new ProfilePhotoResponse(iImagesRepository, user.getProfilePhotoIdentifier()) : null)
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
    public List<UserPreviewResponse> getVerified() {
        return userRepository.findAllByVerified(true).stream()
                .map(u -> {
                    try {
                        return new UserPreviewResponse(u, iImagesRepository);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private UserDetailsByIdentifierResponse responseSessionUserIdentifierBlocked(String sessionUserIdentifier, User targetUser, boolean isBlockedByMe) throws Exception {
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
                .profilePhoto(targetUser.getProfilePhotoIdentifier() != null ? new ProfilePhotoResponse(iImagesRepository, targetUser.getProfilePhotoIdentifier()) : null)
                .build();
    }

    private UserDetailsByIdentifierResponse responseTargetUserIdentifierBlocked(String sessionUserIdentifier, User targetUser) throws Exception {
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
                .profilePhoto(targetUser.getProfilePhotoIdentifier() != null ? new ProfilePhotoResponse(iImagesRepository, targetUser.getProfilePhotoIdentifier()) : null)
                .build();
    }

    private UserDetailsByIdentifierResponse fullResponse(User targetUser, String sessionUser) throws Exception {
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
                .profilePhoto(targetUser.getProfilePhotoIdentifier() != null ? new ProfilePhotoResponse(iImagesRepository, targetUser.getProfilePhotoIdentifier()) : null)
                .build();
    }
}
