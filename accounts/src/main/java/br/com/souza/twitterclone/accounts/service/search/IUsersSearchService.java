package br.com.souza.twitterclone.accounts.service.search;

import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.dto.pagination.CustomPage;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserEmailUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserInfosUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPasswordUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserPrivacyUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserUsernameUpdateRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface IUsersSearchService {

    UserDetailsResponse searchUserInfos(String userIdentifier) throws Exception;
    UserDetailsByIdentifierResponse searchUserInfosByIdentifier(String userIdentifier, String targetUserIdentifier) throws Exception;
    List<UserPreviewResponse> getUsersByUsername(String sessionUserIdentifier, String targetUserIdentifier, Integer page, Integer size) throws Exception;
    List<UserPreviewResponse> getUserFollowers(String sessionUserIdentifier, String targetUsername, Integer page, Integer size) throws Exception;
    List<UserPreviewResponse> getUserFollows(String sessionUserIdentifier, String targetUsername, Integer page, Integer size) throws Exception;

}
