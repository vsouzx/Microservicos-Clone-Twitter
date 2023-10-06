package br.com.souza.twitterclone.accounts.service.interactions.impl;

import br.com.souza.twitterclone.accounts.client.IFeedClient;
import br.com.souza.twitterclone.accounts.database.model.AlertedUsers;
import br.com.souza.twitterclone.accounts.database.model.AlertedUsersId;
import br.com.souza.twitterclone.accounts.database.model.BlockedUsers;
import br.com.souza.twitterclone.accounts.database.model.BlockedUsersId;
import br.com.souza.twitterclone.accounts.database.model.SilencedUsers;
import br.com.souza.twitterclone.accounts.database.model.SilencedUsersId;
import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.model.UsersFollows;
import br.com.souza.twitterclone.accounts.database.model.UsersFollowsId;
import br.com.souza.twitterclone.accounts.database.model.UsersPendingFollows;
import br.com.souza.twitterclone.accounts.database.model.UsersPendingFollowsId;
import br.com.souza.twitterclone.accounts.database.repository.AlertedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.BlockedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.SilencedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.database.repository.UsersFollowsRepository;
import br.com.souza.twitterclone.accounts.database.repository.UsersPendingFollowsRepository;
import br.com.souza.twitterclone.accounts.dto.client.DeleteNotificationRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import br.com.souza.twitterclone.accounts.enums.NotificationsTypeEnum;
import br.com.souza.twitterclone.accounts.handler.exceptions.NonexistentPendingFollowException;
import br.com.souza.twitterclone.accounts.handler.exceptions.UnableToAlertException;
import br.com.souza.twitterclone.accounts.handler.exceptions.UnableToFollowException;
import br.com.souza.twitterclone.accounts.handler.exceptions.UnableToSilenceException;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.accounts.service.client.INotificationsClientService;
import br.com.souza.twitterclone.accounts.service.interactions.IUsersInteractionsService;
import br.com.souza.twitterclone.accounts.service.user.IUserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UsersInteractionsServiceImpl implements IUsersInteractionsService {

    private final UserRepository userRepository;
    private final BlockedUsersRepository blockedUsersRepository;
    private final UsersFollowsRepository usersFollowsRepository;
    private final UsersPendingFollowsRepository usersPendingFollowsRepository;
    private final SilencedUsersRepository silencedUsersRepository;
    private final AlertedUsersRepository alertedUsersRepository;
    private final INotificationsClientService iNotificationsClientService;
    private final IFeedClient iFeedClient;
    private final IUserService iUserService;

    public UsersInteractionsServiceImpl(UserRepository userRepository,
                                        BlockedUsersRepository blockedUsersRepository,
                                        UsersFollowsRepository usersFollowsRepository,
                                        UsersPendingFollowsRepository usersPendingFollowsRepository,
                                        SilencedUsersRepository silencedUsersRepository,
                                        AlertedUsersRepository alertedUsersRepository,
                                        INotificationsClientService iNotificationsClientService,
                                        IFeedClient iFeedClient,
                                        IUserService iUserService) {
        this.userRepository = userRepository;
        this.blockedUsersRepository = blockedUsersRepository;
        this.usersFollowsRepository = usersFollowsRepository;
        this.usersPendingFollowsRepository = usersPendingFollowsRepository;
        this.silencedUsersRepository = silencedUsersRepository;
        this.alertedUsersRepository = alertedUsersRepository;
        this.iNotificationsClientService = iNotificationsClientService;
        this.iFeedClient = iFeedClient;
        this.iUserService = iUserService;
    }

    @Override
    public void blockToggle(String sessionUserIdentifier, String targetUserIdentifier) throws Exception {
        User targetUser = iUserService.findUserByUsernameOrEmailOrIdentifier(targetUserIdentifier);

        Optional<BlockedUsers> targetUserIsBlocked = verifyIfIsBlocked(sessionUserIdentifier, targetUser.getIdentifier());

        if (targetUserIsBlocked.isPresent()) {
            blockedUsersRepository.delete(targetUserIsBlocked.get());
        } else {
            //Verificando se ambos usuarios se seguem/tem solicitacao pendente. Caso sim, é necessário remover antes de bloquear
            verifyIfIsFollowing(sessionUserIdentifier, targetUser.getIdentifier())
                    .ifPresent(usersFollowsRepository::delete);

            verifyIfIsPendingFollowing(sessionUserIdentifier, targetUser.getIdentifier())
                    .ifPresent(usersPendingFollowsRepository::delete);

            verifyIfIsFollowing(targetUser.getIdentifier(), sessionUserIdentifier)
                    .ifPresent(usersFollowsRepository::delete);

            verifyIfIsPendingFollowing(targetUser.getIdentifier(), sessionUserIdentifier)
                    .ifPresent(usersPendingFollowsRepository::delete);

            blockedUsersRepository.save(BlockedUsers.builder()
                    .id(BlockedUsersId.builder()
                            .blockerIdentifier(sessionUserIdentifier)
                            .blockedIdentifier(targetUser.getIdentifier())
                            .build())
                    .build());
        }
    }

    @Override
    public void followToggle(String sessionUserIdentifier, String targetUserIdentifier, String authorization) throws Exception {
        User targetUser = iUserService.findUserByUsernameOrEmailOrIdentifier(targetUserIdentifier);

        Optional<BlockedUsers> targetUserIsBlocked = verifyIfIsBlocked(sessionUserIdentifier, targetUser.getIdentifier());
        if (targetUserIsBlocked.isPresent()) {
            throw new UnableToFollowException();
        }

        Optional<BlockedUsers> sessionUserIsBlocked = verifyIfIsBlocked(targetUser.getIdentifier(), sessionUserIdentifier);
        if (sessionUserIsBlocked.isPresent()) {
            throw new UnableToFollowException();
        }

        Optional<UsersFollows> targetUserIsFollowed = verifyIfIsFollowing(sessionUserIdentifier, targetUser.getIdentifier());
        Optional<UsersPendingFollows> targetUserIsPendingFollowed = verifyIfIsPendingFollowing(sessionUserIdentifier, targetUser.getIdentifier());

        //se o sessionUserIdentifier estiver seguindo o targetUserIdentifier, vai dar unfollow
        if (targetUserIsFollowed.isPresent()) {
            iNotificationsClientService.deleteNotification(
                    DeleteNotificationRequest.builder()
                            .tweetIdentifier(null)
                            .userSenderIdentifier(sessionUserIdentifier)
                            .userReceiverIdentifier(targetUser.getIdentifier())
                            .typeDescription(NotificationsTypeEnum.NEW_FOLLOWER.toString())
                            .build(),
                    authorization
            );

            usersFollowsRepository.delete(targetUserIsFollowed.get());
        } //se o sessionUserIdentifier estiver com solicitação pendente para o targetUserIdentifier, vai cancelar solicitação
        else if (targetUserIsPendingFollowed.isPresent()) {
            usersPendingFollowsRepository.delete(targetUserIsPendingFollowed.get());
        } //se o sessionUserIdentifier não seguir e nem estiver com solicitação pendente para o targetUserIdentifier, vai seguir/mandar solicitação
        else {
            if (targetUser.getPrivateAccount()) {
                usersPendingFollowsRepository.save(UsersPendingFollows.builder()
                        .id(UsersPendingFollowsId.builder()
                                .pendingFollowerIdentifier(sessionUserIdentifier)
                                .pendingFollowedIdentifier(targetUser.getIdentifier())
                                .build())
                        .build());
            } else {
                usersFollowsRepository.save(UsersFollows.builder()
                        .id(UsersFollowsId.builder()
                                .followerIdentifier(sessionUserIdentifier)
                                .followedIdentifier(targetUser.getIdentifier())
                                .build())
                        .build());

                iNotificationsClientService.createNewNotification(
                        sessionUserIdentifier,
                        targetUser.getIdentifier(),
                        NotificationsTypeEnum.NEW_FOLLOWER.toString(),
                        null,
                        authorization
                );
            }
        }
    }

    @Override
    public void pendingFollowAcceptDecline(String sessionUserIdentifier, String targetIdentifier, boolean isAccept) throws Exception {
        User targetUser = iUserService.findUserByUsernameOrEmailOrIdentifier(targetIdentifier);

        UsersPendingFollows pendingFollowRequest = verifyIfIsPendingFollowing(targetUser.getIdentifier(), sessionUserIdentifier)
                .orElseThrow(NonexistentPendingFollowException::new);

        if (isAccept) {
            usersPendingFollowsRepository.delete(pendingFollowRequest);
            usersFollowsRepository.save(UsersFollows.builder()
                    .id(UsersFollowsId.builder()
                            .followerIdentifier(targetUser.getIdentifier())
                            .followedIdentifier(sessionUserIdentifier)
                            .build())
                    .build());
        } else {
            usersPendingFollowsRepository.delete(pendingFollowRequest);
        }
    }

    @Override
    public void silencetoggle(String sessionUserIdentifier, String targetUserIdentifier) throws Exception {
        User targetUser = iUserService.findUserByUsernameOrEmailOrIdentifier(targetUserIdentifier);

        Optional<BlockedUsers> targetUserIsBlocked = verifyIfIsBlocked(sessionUserIdentifier, targetUser.getIdentifier());
        if (targetUserIsBlocked.isPresent()) {
            throw new UnableToSilenceException();
        }

        Optional<BlockedUsers> sessionUserIsBlocked = verifyIfIsBlocked(targetUser.getIdentifier(), sessionUserIdentifier);
        if (sessionUserIsBlocked.isPresent()) {
            throw new UnableToSilenceException();
        }

        verifyIfIsSilenced(sessionUserIdentifier, targetUser.getIdentifier())
                .ifPresentOrElse(silencedUsersRepository::delete, () -> {
                    silencedUsersRepository.save(SilencedUsers.builder()
                            .id(SilencedUsersId.builder()
                                    .silencerIdentifier(sessionUserIdentifier)
                                    .silencedIdentifier(targetUser.getIdentifier())
                                    .build())
                            .build());
                });
    }

    @Override
    public void alertToggle(String sessionUserIdentifier, String targetUserIdentifier) throws Exception {
        User targetUser = iUserService.findUserByUsernameOrEmailOrIdentifier(targetUserIdentifier);

        Optional<BlockedUsers> targetUserIsBlocked = verifyIfIsBlocked(sessionUserIdentifier, targetUser.getIdentifier());
        if (targetUserIsBlocked.isPresent()) {
            throw new UnableToFollowException();
        }

        Optional<BlockedUsers> sessionUserIsBlocked = verifyIfIsBlocked(targetUser.getIdentifier(), sessionUserIdentifier);
        if (sessionUserIsBlocked.isPresent()) {
            throw new UnableToFollowException();
        }

        verifyIfIsFollowing(sessionUserIdentifier, targetUser.getIdentifier())
                .orElseThrow(UnableToAlertException::new);

        Optional<AlertedUsers> targetUserIsAlerted = verifyIfIsAlerted(sessionUserIdentifier, targetUser.getIdentifier());

        if (targetUserIsAlerted.isPresent()) {
            alertedUsersRepository.delete(targetUserIsAlerted.get());
        } else {
            alertedUsersRepository.save(AlertedUsers.builder()
                    .id(AlertedUsersId.builder()
                            .alerterIdentifier(sessionUserIdentifier)
                            .alertedIdentifier(targetUser.getIdentifier())
                            .build())
                    .build());
        }
    }

    @Override
    public Boolean anyoneIsBlocked(String sessionUserIdentifier, String targetUserIdentifier) throws Exception {
        User targetUser = iUserService.findUserByUsernameOrEmailOrIdentifier(targetUserIdentifier);

        Optional<BlockedUsers> targetUserIsBlocked = verifyIfIsBlocked(sessionUserIdentifier, targetUser.getIdentifier());
        Optional<BlockedUsers> sessionUserIsBlocked = verifyIfIsBlocked(targetUser.getIdentifier(), sessionUserIdentifier);

        return targetUserIsBlocked.isPresent() || sessionUserIsBlocked.isPresent();
    }

    @Override
    public Optional<UsersFollows> verifyIfIsFollowing(String follower, String followed) {
        return usersFollowsRepository.findById(UsersFollowsId.builder()
                .followerIdentifier(follower)
                .followedIdentifier(followed)
                .build());
    }

    @Override
    public Optional<UsersPendingFollows> verifyIfIsPendingFollowing(String follower, String followed) {
        return usersPendingFollowsRepository.findById(UsersPendingFollowsId.builder()
                .pendingFollowerIdentifier(follower)
                .pendingFollowedIdentifier(followed)
                .build());
    }

    @Override
    public Optional<BlockedUsers> verifyIfIsBlocked(String blocker, String blocked) {
        return blockedUsersRepository.findById(BlockedUsersId.builder()
                .blockerIdentifier(blocker)
                .blockedIdentifier(blocked)
                .build());
    }

    @Override
    public Optional<SilencedUsers> verifyIfIsSilenced(String silencer, String silenced) {
        return silencedUsersRepository.findById(SilencedUsersId.builder()
                .silencerIdentifier(silencer)
                .silencedIdentifier(silenced)
                .build());
    }

    @Override
    public Optional<AlertedUsers> verifyIfIsAlerted(String follower, String followed) {
        return alertedUsersRepository.findById(AlertedUsersId.builder()
                .alerterIdentifier(follower)
                .alertedIdentifier(followed)
                .build());
    }

    @Override
    public Integer getUserFollowersCount(String targetUserIdentifier) {
        List<User> followers = userRepository.findUserFollowers(targetUserIdentifier);
        return followers.size();
    }

    @Override
    public Integer getUserFollowsCount(String user) {
        List<User> followers = userRepository.findUserFollows(user);
        return followers.size();
    }

    @Override
    public List<UserPreviewResponse> getCommonFollowers(String sessionUser, String targetUser) throws Exception {
        List<User> followers = userRepository.findSessionUserCommonFollowsWithTargerUser(sessionUser, targetUser);
        return rowMapper(followers, sessionUser);
    }

    @Override
    public Integer getTweetsCount(String targetUserIdentifier, String authorization) {
        return iFeedClient.getTweetsCount(targetUserIdentifier, authorization);
    }

    private List<UserPreviewResponse> rowMapper(List<User> followers, String sessionUserIdentifier) throws Exception {
        List<UserPreviewResponse> response = new ArrayList<>();
        for (User follower : followers) {
            response.add(UserPreviewResponse.builder()
                    .username(follower.getUsername())
                    .firstName(follower.getFirstName())
                    .biography(follower.getBiography())
                    .privateAccount(follower.getPrivateAccount())
                    .profilePhotoUrl(follower.getProfilePhotoUrl())
                    .isFollowedByMe(verifyIfIsFollowing(sessionUserIdentifier, follower.getIdentifier()).isPresent())
                    .isFollowingMe(verifyIfIsFollowing(follower.getIdentifier(), sessionUserIdentifier).isPresent())
                    .build());
        }
        return response;
    }
}
