package br.com.souza.twitterclone.accounts.service.interactions;

import br.com.souza.twitterclone.accounts.database.model.BlockedUsers;
import br.com.souza.twitterclone.accounts.database.model.SilencedUsers;
import br.com.souza.twitterclone.accounts.database.model.UsersFollows;
import br.com.souza.twitterclone.accounts.database.model.UsersPendingFollows;
import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import java.util.List;
import java.util.Optional;

public interface IUsersInteractionsService {

    void blockToggle(String sessionUserIdentifier, String targetUserIdentifier) throws Exception;

    void followToggle(String sessionUserIdentifier, String targetUserIdentifier) throws Exception;

    void pendingFollowAcceptDecline(String sessionUserIdentifier, String targetUserIdentifier, boolean accept) throws Exception;

    void silencetoggle(String sessionUserIdentifier, String targetUserIdentifier) throws Exception;

    Optional<UsersFollows> verifyIfIsFollowing(String follower, String followed);

    Optional<UsersPendingFollows> verifyIfIsPendingFollowing(String follower, String followed);

    Optional<BlockedUsers> verifyIfIsBlocked(String blocker, String blocked);

    Optional<SilencedUsers> verifyIfIsSilenced(String silencer, String silenced);

    Integer getUserFollowersCount(String sessionUserIdentifier, String targetUserIdentifier);

    Integer getUserFollowsCount(String sessionUserIdentifier, String targetUserIdentifier);

    List<UserPreviewResponse> getCommonFollowers(String sessionUser, String targetUser);


}
