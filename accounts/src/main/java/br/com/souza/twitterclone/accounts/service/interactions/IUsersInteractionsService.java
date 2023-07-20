package br.com.souza.twitterclone.accounts.service.interactions;

import br.com.souza.twitterclone.accounts.database.model.BlockedUsers;
import br.com.souza.twitterclone.accounts.database.model.UsersFollows;
import br.com.souza.twitterclone.accounts.database.model.UsersPendingFollows;
import br.com.souza.twitterclone.accounts.dto.user.UserEmailUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserInfosUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPasswordUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserPrivacyUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserUsernameUpdateRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface IUsersInteractionsService {

    void blockToggle(String sessionUserIdentifier, String targetUserIdentifier) throws Exception;

    void followToggle(String sessionUserIdentifier, String targetUserIdentifier) throws Exception;

    void pendingFollowAcceptDecline(String sessionUserIdentifier, String targetUserIdentifier, boolean accept) throws Exception;

    Optional<UsersFollows> verifyIfIsFollowing(String follower, String followed);

    Optional<UsersPendingFollows> verifyIfIsPendingFollowing(String follower, String followed);

    Optional<BlockedUsers> verifyIfIsBlocked(String blocker, String blocked);

    List<UserPreviewResponse> getUserFollowers(String user);

    List<UserPreviewResponse> getUserFollows(String user);

    boolean isFollowing(String sessionUser, String targetUser);

    boolean isPendingFollowing(String sessionUser, String targetUser);

    List<UserPreviewResponse> getCommonFollowers(String sessionUser, String targetUser);

}
