package br.com.souza.twitterclone.accounts.service.interactions.impl;

import br.com.souza.twitterclone.accounts.database.model.BlockedUsers;
import br.com.souza.twitterclone.accounts.database.model.BlockedUsersId;
import br.com.souza.twitterclone.accounts.database.model.SilencedUsers;
import br.com.souza.twitterclone.accounts.database.model.SilencedUsersId;
import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.model.UsersFollows;
import br.com.souza.twitterclone.accounts.database.model.UsersFollowsId;
import br.com.souza.twitterclone.accounts.database.model.UsersPendingFollows;
import br.com.souza.twitterclone.accounts.database.model.UsersPendingFollowsId;
import br.com.souza.twitterclone.accounts.database.repository.BlockedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.SilencedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.database.repository.UsersFollowsRepository;
import br.com.souza.twitterclone.accounts.database.repository.UsersPendingFollowsRepository;
import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import br.com.souza.twitterclone.accounts.handler.exceptions.NonexistentPendingFollowException;
import br.com.souza.twitterclone.accounts.handler.exceptions.UnableToFollowException;
import br.com.souza.twitterclone.accounts.handler.exceptions.UnableToSilenceException;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.accounts.service.interactions.IUsersInteractionsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsersInteractionsServiceImpl implements IUsersInteractionsService {

    private final UserRepository userRepository;
    private final BlockedUsersRepository blockedUsersRepository;
    private final UsersFollowsRepository usersFollowsRepository;
    private final UsersPendingFollowsRepository usersPendingFollowsRepository;
    private final SilencedUsersRepository silencedUsersRepository;

    public UsersInteractionsServiceImpl(UserRepository userRepository,
                                        BlockedUsersRepository blockedUsersRepository,
                                        UsersFollowsRepository usersFollowsRepository,
                                        UsersPendingFollowsRepository usersPendingFollowsRepository,
                                        SilencedUsersRepository silencedUsersRepository) {
        this.userRepository = userRepository;
        this.blockedUsersRepository = blockedUsersRepository;
        this.usersFollowsRepository = usersFollowsRepository;
        this.usersPendingFollowsRepository = usersPendingFollowsRepository;
        this.silencedUsersRepository = silencedUsersRepository;
    }

    @Override
    public void blockToggle(String sessionUserIdentifier, String targetUserIdentifier) throws Exception {
        Optional<User> user = userRepository.findById(targetUserIdentifier);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        Optional<BlockedUsers> targetUserIsBlocked = verifyIfIsBlocked(sessionUserIdentifier, targetUserIdentifier);

        if (targetUserIsBlocked.isPresent()) {
            blockedUsersRepository.delete(targetUserIsBlocked.get());
        } else {

            //Verificando se ambos usuarios se seguem/tem solicitacao pendente. Caso sim, é necessário remover antes de bloquear
            Optional<UsersFollows> targetUserIsFollowed = verifyIfIsFollowing(sessionUserIdentifier, targetUserIdentifier);
            targetUserIsFollowed.ifPresent(usersFollowsRepository::delete);

            Optional<UsersPendingFollows> targetUserIsPendingFollowed = verifyIfIsPendingFollowing(sessionUserIdentifier, targetUserIdentifier);
            targetUserIsPendingFollowed.ifPresent(usersPendingFollowsRepository::delete);

            Optional<UsersFollows> sessionUserIsFollowed = verifyIfIsFollowing(targetUserIdentifier, sessionUserIdentifier);
            sessionUserIsFollowed.ifPresent(usersFollowsRepository::delete);

            Optional<UsersPendingFollows> sessionUserIsPendingFollowed = verifyIfIsPendingFollowing(targetUserIdentifier, sessionUserIdentifier);
            sessionUserIsPendingFollowed.ifPresent(usersPendingFollowsRepository::delete);

            blockedUsersRepository.save(BlockedUsers.builder()
                    .id(BlockedUsersId.builder()
                            .blockerIdentifier(sessionUserIdentifier)
                            .blockedIdentifier(targetUserIdentifier)
                            .build())
                    .build());
        }
    }

    @Override
    public void followToggle(String sessionUserIdentifier, String targetUserIdentifier) throws Exception {
        Optional<User> targetUser = userRepository.findById(targetUserIdentifier);

        if (targetUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        Optional<BlockedUsers> targetUserIsBlocked = verifyIfIsBlocked(sessionUserIdentifier, targetUserIdentifier);
        if (targetUserIsBlocked.isPresent()) {
            throw new UnableToFollowException();
        }

        Optional<BlockedUsers> sessionUserIsBlocked = verifyIfIsBlocked(targetUserIdentifier, sessionUserIdentifier);
        if (sessionUserIsBlocked.isPresent()) {
            throw new UnableToFollowException();
        }

        Optional<UsersFollows> targetUserIsFollowed = verifyIfIsFollowing(sessionUserIdentifier, targetUserIdentifier);
        Optional<UsersPendingFollows> targetUserIsPendingFollowed = verifyIfIsPendingFollowing(sessionUserIdentifier, targetUserIdentifier);

        //se o sessionUserIdentifier estiver seguindo o targetUserIdentifier, vai dar unfollow
        if (targetUserIsFollowed.isPresent()) {
            usersFollowsRepository.delete(targetUserIsFollowed.get());
        } //se o sessionUserIdentifier estiver com solicitação pendente para o targetUserIdentifier, vai cancelar solicitação
        else if (targetUserIsPendingFollowed.isPresent()) {
            usersPendingFollowsRepository.delete(targetUserIsPendingFollowed.get());
        } //se o sessionUserIdentifier não seguir e nem estiver com solicitação pendente para o targetUserIdentifier, vai seguir/mandar solicitação
        else {
            if (targetUser.get().getPrivateAccount()) {
                usersPendingFollowsRepository.save(UsersPendingFollows.builder()
                        .id(UsersPendingFollowsId.builder()
                                .pendingFollowerIdentifier(sessionUserIdentifier)
                                .pendingFollowedIdentifier(targetUserIdentifier)
                                .build())
                        .build());
            } else {
                usersFollowsRepository.save(UsersFollows.builder()
                        .id(UsersFollowsId.builder()
                                .followerIdentifier(sessionUserIdentifier)
                                .followedIdentifier(targetUserIdentifier)
                                .build())
                        .build());
            }
        }
    }

    @Override
    public void pendingFollowAcceptDecline(String sessionUserIdentifier, String targetIdentifier, boolean isAccept) throws Exception {
        Optional<UsersPendingFollows> pendingFollowRequest = verifyIfIsPendingFollowing(targetIdentifier, sessionUserIdentifier);

        if (pendingFollowRequest.isEmpty()) {
            throw new NonexistentPendingFollowException();
        }

        if (isAccept) {
            usersPendingFollowsRepository.delete(pendingFollowRequest.get());
            usersFollowsRepository.save(UsersFollows.builder()
                    .id(UsersFollowsId.builder()
                            .followerIdentifier(targetIdentifier)
                            .followedIdentifier(sessionUserIdentifier)
                            .build())
                    .build());
        }else {
            usersPendingFollowsRepository.delete(pendingFollowRequest.get());
        }
    }

    @Override
    public void silencetoggle(String sessionUserIdentifier, String targetUserIdentifier) throws Exception {
        Optional<User> targetUser = userRepository.findById(targetUserIdentifier);

        if (targetUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        Optional<BlockedUsers> targetUserIsBlocked = verifyIfIsBlocked(sessionUserIdentifier, targetUserIdentifier);
        if (targetUserIsBlocked.isPresent()) {
            throw new UnableToSilenceException();
        }

        Optional<BlockedUsers> sessionUserIsBlocked = verifyIfIsBlocked(targetUserIdentifier, sessionUserIdentifier);
        if (sessionUserIsBlocked.isPresent()) {
            throw new UnableToSilenceException();
        }

        Optional<SilencedUsers> isSilenced = verifyIfIsSilenced(sessionUserIdentifier, targetUserIdentifier);

        if(isSilenced.isPresent()){
            silencedUsersRepository.delete(isSilenced.get());
        }else{
            silencedUsersRepository.save(SilencedUsers.builder()
                    .id(SilencedUsersId.builder()
                            .silencerIdentifier(sessionUserIdentifier)
                            .silencedIdentifier(targetUserIdentifier)
                            .build())
                    .build());
        }
    }

    @Override
    public Boolean anyoneIsBlocked(String sessionUserIdentifier, String targetUserIdentifier) throws Exception {
        Optional<User> targetUser = userRepository.findById(targetUserIdentifier);

        if (targetUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        Optional<BlockedUsers> targetUserIsBlocked = verifyIfIsBlocked(sessionUserIdentifier, targetUserIdentifier);
        Optional<BlockedUsers> sessionUserIsBlocked = verifyIfIsBlocked(targetUserIdentifier, sessionUserIdentifier);

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
    public Integer getUserFollowersCount(String sessionUserIdentifier, String targetUserIdentifier){
        List<User> followers = userRepository.findUserFollowers(targetUserIdentifier);
        return followers.size();
    }

    @Override
    public Integer getUserFollowsCount(String sessionUserIdentifier, String user){
        List<User> followers = userRepository.findUserFollows(user);
        return followers.size();
    }

    @Override
    public List<UserPreviewResponse> getCommonFollowers(String sessionUser, String targetUser) {
        List<User> followers = userRepository.findSessionUserCommonFollowsWithTargerUser(sessionUser, targetUser);
        return rowMapper(followers, sessionUser);
    }

    private List<UserPreviewResponse> rowMapper(List<User> followers, String sessionUserIdentifier){
        List<UserPreviewResponse> response = new ArrayList<>();

        if(!followers.isEmpty()){
            followers.stream().forEach(f -> {
                response.add(UserPreviewResponse.builder()
                        .username(f.getUsername())
                        .firstName(f.getFirstName())
                        .biography(f.getBiography())
                        .privateAccount(f.getPrivateAccount())
                        .profilePhoto(f.getProfilePhoto())
                        .isFollowedByMe(verifyIfIsFollowing(sessionUserIdentifier, f.getIdentifier()).isPresent())
                        .isFollowingMe(verifyIfIsFollowing(f.getIdentifier(), sessionUserIdentifier).isPresent())
                        .build());
            });
        }
        return response;
    }
}
