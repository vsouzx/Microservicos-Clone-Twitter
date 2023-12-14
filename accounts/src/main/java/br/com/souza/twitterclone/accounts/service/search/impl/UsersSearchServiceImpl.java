package br.com.souza.twitterclone.accounts.service.search.impl;

import br.com.souza.twitterclone.accounts.database.model.BlockedUsersId;
import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.model.UsersSearchHistoric;
import br.com.souza.twitterclone.accounts.database.repository.AlertedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.BlockedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.database.repository.UsersSearchHistoricRepository;
import br.com.souza.twitterclone.accounts.database.repository.followsdetails.IFollowsDetailsStrategy;
import br.com.souza.twitterclone.accounts.database.repository.followsdetails.factory.FollowsDetailsStrategyFactory;
import br.com.souza.twitterclone.accounts.database.repository.impl.AllUserKnownFollowersRepository;
import br.com.souza.twitterclone.accounts.database.repository.impl.UsersRepositoryImpl;
import br.com.souza.twitterclone.accounts.database.repository.impl.WhoToFollowRepositoryImpl;
import br.com.souza.twitterclone.accounts.dto.user.FollowsAndFollowersResponse;
import br.com.souza.twitterclone.accounts.dto.user.KnownUsersResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserSearchHistoricResponse;
import br.com.souza.twitterclone.accounts.dto.user.ValidEmailResponse;
import br.com.souza.twitterclone.accounts.dto.user.ValidUserResponse;
import br.com.souza.twitterclone.accounts.dto.user.ValidUsernameResponse;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.accounts.service.interactions.IUsersInteractionsService;
import br.com.souza.twitterclone.accounts.service.search.IUsersSearchService;
import br.com.souza.twitterclone.accounts.service.user.IUserService;
import br.com.souza.twitterclone.accounts.util.UsefulDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    private final IUserService iUserService;
    private final FollowsDetailsStrategyFactory followsDetailsStrategyFactory;
    private final AllUserKnownFollowersRepository allUserKnownFollowersRepository;
    private final UsersSearchHistoricRepository usersSearchHistoricRepository;
    private static final Integer MAX_HISTORIC_SIZE = 15;

    public UsersSearchServiceImpl(UserRepository userRepository,
                                  UsersRepositoryImpl usersRepositoryImpl,
                                  BlockedUsersRepository blockedUsersRepository,
                                  IUsersInteractionsService iUsersInteractionsService,
                                  AlertedUsersRepository alertedUsersRepository,
                                  WhoToFollowRepositoryImpl whoToFollowRepository,
                                  IUserService iUserService,
                                  FollowsDetailsStrategyFactory followsDetailsStrategyFactory,
                                  AllUserKnownFollowersRepository allUserKnownFollowersRepository,
                                  UsersSearchHistoricRepository usersSearchHistoricRepository) {
        this.userRepository = userRepository;
        this.usersRepositoryImpl = usersRepositoryImpl;
        this.blockedUsersRepository = blockedUsersRepository;
        this.iUsersInteractionsService = iUsersInteractionsService;
        this.alertedUsersRepository = alertedUsersRepository;
        this.whoToFollowRepository = whoToFollowRepository;
        this.iUserService = iUserService;
        this.followsDetailsStrategyFactory = followsDetailsStrategyFactory;
        this.allUserKnownFollowersRepository = allUserKnownFollowersRepository;
        this.usersSearchHistoricRepository = usersSearchHistoricRepository;
    }

    @Override
    public UserDetailsResponse searchUserInfos(String sessionUserIdentifier) throws Exception {
        User user = iUserService.findUserByUsernameOrEmailOrIdentifier(sessionUserIdentifier);

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
                .tweetsCount(iUsersInteractionsService.getTweetsCount(user.getIdentifier()))
                .isVerified(user.getVerified())
                .build();
    }

    @Override
    public UserDetailsByIdentifierResponse searchUserInfosByIdentifier(String sessionUserIdentifier, String targetUserIdentifier) throws Exception {
        User targetUser = iUserService.findUserByUsernameOrEmailOrIdentifier(targetUserIdentifier);

        boolean isSessionUserIdentifierBlocked = blockedUsersRepository.findById(BlockedUsersId.builder()
                .blockerIdentifier(targetUser.getIdentifier())
                .blockedIdentifier(sessionUserIdentifier)
                .build()).isPresent();

        boolean targetUserIdentifierBlocked = blockedUsersRepository.findById(BlockedUsersId.builder()
                .blockerIdentifier(sessionUserIdentifier)
                .blockedIdentifier(targetUser.getIdentifier())
                .build()).isPresent();

        if (isSessionUserIdentifierBlocked) {
            return responseSessionUserIdentifierBlocked(targetUser, targetUserIdentifierBlocked);
        }
        if (targetUserIdentifierBlocked) {
            return responseTargetUserIdentifierBlocked(targetUser);
        }
        return fullResponse(targetUser, sessionUserIdentifier);
    }

    @Override
    public List<UserDetailsByIdentifierResponse> getUsersByUsername(String sessionUserIdentifier, String targetUsername, Integer page, Integer size) {
        return usersRepositoryImpl.findAllByUsername(sessionUserIdentifier, targetUsername, page, size);
    }

    @Override
    public List<UserDetailsByIdentifierResponse> getUserFollowsDetails(String sessionUserIdentifier, String targetUserIdentifier, String type, Integer page, Integer size) throws Exception {
        User user = iUserService.findUserByUsernameOrEmailOrIdentifier(targetUserIdentifier);
        IFollowsDetailsStrategy strategy = followsDetailsStrategyFactory.getStrategy(type);
        return strategy.getUserFollowsInformations(sessionUserIdentifier, user.getIdentifier(), page, size);
    }

    @Override
    public List<UserPreviewResponse> getUserPendingFollowers(String sessionUserIdentifier, Integer page, Integer size) throws Exception {
        return usersRepositoryImpl.getUserPendingFollowers(sessionUserIdentifier, page, size);
    }

    @Override
    public ValidEmailResponse isValidEmail(String email) {
        return ValidEmailResponse.builder()
                .isValidEmail(userRepository.findByEmail(email).isEmpty())
                .build();
    }

    @Override
    public ValidUsernameResponse isValidUsername(String username) {
        return ValidUsernameResponse.builder()
                .isValidUsername(userRepository.findByUsername(username).isEmpty())
                .build();
    }

    @Override
    public ValidUserResponse isValidUser(String username) {
        Optional<User> user;

        user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return ValidUserResponse.builder()
                    .isValidUser(true)
                    .isUsername(true)
                    .isEmail(false)
                    .build();
        }

        user = userRepository.findByEmail(username);
        if (user.isPresent()) {
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
    public List<UserDetailsByIdentifierResponse> getWhoToFollow(String sessionUserIdentifier, Integer page, Integer size, String userOnScreen, Boolean isVerified) throws Exception {
        User user = iUserService.findUserByUsernameOrEmailOrIdentifier(userOnScreen == null || userOnScreen.isBlank() ? sessionUserIdentifier : userOnScreen);
        return whoToFollowRepository.find(sessionUserIdentifier, page, size, user.getIdentifier(), isVerified);
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
    public List<String> getAlertedUsers(String sessionUserIdentifier) {
        return alertedUsersRepository.findAllByIdAlertedIdentifier(sessionUserIdentifier).stream()
                .map(user -> user.getId().getAlerterIdentifier())
                .toList();
    }

    @Override
    public FollowsAndFollowersResponse getFollowsAndFollowers(String targetUserIdentifier) {
        return FollowsAndFollowersResponse.builder()
                .followers(iUsersInteractionsService.getUserFollowersCount(targetUserIdentifier))
                .follows(iUsersInteractionsService.getUserFollowsCount(targetUserIdentifier))
                .build();
    }

    @Override
    public List<KnownUsersResponse> getAllKnownFollowers(String sessionUserIdentifier, String targetUserIdentifier) throws Exception {
        User targetUser = iUserService.findUserByUsernameOrEmailOrIdentifier(targetUserIdentifier);
        return allUserKnownFollowersRepository.getUserFollowsInformations(sessionUserIdentifier, targetUser.getIdentifier());
    }

    @Override
    public List<UserSearchHistoricResponse> getUserSearchHistoric(String sessionUserIdentifier) throws Exception {
        return usersSearchHistoricRepository.findAllBySearcherIdentifier(sessionUserIdentifier).stream()
                .map(h -> {
                    try {
                        return new UserSearchHistoricResponse(h, iUserService);
                    } catch (UserNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .sorted(Comparator.comparing(UserSearchHistoricResponse::getSearchDate).reversed())
                .toList();
    }

    @Override
    public void saveUserSearchHistoric(String sessionUserIdentifier, String targetUserIdentifier, String text) throws Exception {

        if (text == null && targetUserIdentifier == null) {
            throw new Exception("Text or UserIdentifier must have value");
        }

        List<UsersSearchHistoric> usersSearchHistoric = usersSearchHistoricRepository.findAllBySearcherIdentifier(sessionUserIdentifier);
        if (usersSearchHistoric.size() >= MAX_HISTORIC_SIZE) {
            UsersSearchHistoric older = usersSearchHistoric.stream()
                    .min(Comparator.comparing(UsersSearchHistoric::getSearchDate))
                    .get();
            usersSearchHistoricRepository.delete(older);
        }

        if (targetUserIdentifier != null) {
            User targetUser = iUserService.findUserByUsernameOrEmailOrIdentifier(targetUserIdentifier);;
            Optional<UsersSearchHistoric> possivelHistorico = usersSearchHistoric.stream()
                    .filter(h -> h.getSearchedIdentifier() != null)
                    .filter(h -> h.getSearchedIdentifier().equals(targetUser.getIdentifier()))
                    .findAny();

            if (possivelHistorico.isPresent()) {
                possivelHistorico.get().setSearchDate(UsefulDate.now());
                usersSearchHistoricRepository.save(possivelHistorico.get());
            }else{
                usersSearchHistoricRepository.save(UsersSearchHistoric.builder()
                        .identifier(UUID.randomUUID().toString())
                        .searcherIdentifier(sessionUserIdentifier)
                        .searchedIdentifier(targetUser.getIdentifier())
                        .text(null)
                        .searchDate(UsefulDate.now())
                        .build());
            }
        }

        if (text != null) {
            Optional<UsersSearchHistoric> possivelHistorico = usersSearchHistoric.stream()
                    .filter(h -> h.getText() != null)
                    .filter(h -> h.getText().equals(text))
                    .findAny();

            if (possivelHistorico.isPresent()) {
                possivelHistorico.get().setSearchDate(UsefulDate.now());
                usersSearchHistoricRepository.save(possivelHistorico.get());
            }else {
                usersSearchHistoricRepository.save(UsersSearchHistoric.builder()
                        .identifier(UUID.randomUUID().toString())
                        .searcherIdentifier(sessionUserIdentifier)
                        .searchedIdentifier(null)
                        .text(text)
                        .searchDate(UsefulDate.now())
                        .build());
            }
        }
    }

    @Override
    public void deleteUserSearchHistoric(String historicIdentifier) throws Exception {
        usersSearchHistoricRepository.findById(historicIdentifier)
                        .orElseThrow(() -> new Exception("Historic not found"));
        usersSearchHistoricRepository.deleteById(historicIdentifier);
    }

    @Override
    public void deleteAllUserSearchHistoric(String sessionUserIdentifier) throws Exception {
        List<UsersSearchHistoric> usersSearchHistoric = usersSearchHistoricRepository.findAllBySearcherIdentifier(sessionUserIdentifier);
        usersSearchHistoricRepository.deleteAll(usersSearchHistoric);
    }

    private UserDetailsByIdentifierResponse responseSessionUserIdentifierBlocked(User targetUser, boolean isBlockedByMe) throws Exception {
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
                .tweetsCount(iUsersInteractionsService.getTweetsCount(targetUser.getIdentifier()))
                .build();
    }

    private UserDetailsByIdentifierResponse responseTargetUserIdentifierBlocked(User targetUser) throws Exception {
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
                .tweetsCount(iUsersInteractionsService.getTweetsCount(targetUser.getIdentifier()))
                .build();
    }

    private UserDetailsByIdentifierResponse fullResponse(User targetUser, String sessionUser) throws Exception {
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
                .tweetsCount(iUsersInteractionsService.getTweetsCount(targetUser.getIdentifier()))
                .isVerified(targetUser.getVerified())
                .build();
    }
}
