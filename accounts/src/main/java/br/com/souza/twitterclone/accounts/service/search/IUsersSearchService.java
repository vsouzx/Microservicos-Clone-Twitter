package br.com.souza.twitterclone.accounts.service.search;

import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import br.com.souza.twitterclone.accounts.dto.user.ValidEmailResponse;

import java.util.List;

public interface IUsersSearchService {

    UserDetailsResponse searchUserInfos(String userIdentifier) throws Exception;
    UserDetailsByIdentifierResponse searchUserInfosByIdentifier(String userIdentifier, String targetUserIdentifier) throws Exception;
    List<UserPreviewResponse> getUsersByUsername(String sessionUserIdentifier, String targetUserIdentifier, Integer page, Integer size) throws Exception;
    List<UserPreviewResponse> getUserFollowers(String sessionUserIdentifier, String targetUsername, Integer page, Integer size) throws Exception;
    List<UserPreviewResponse> getUserFollows(String sessionUserIdentifier, String targetUsername, Integer page, Integer size) throws Exception;
    List<UserPreviewResponse> getUserPendingFollowers(String sessionUserIdentifier, Integer page, Integer size) throws Exception;
    ValidEmailResponse isValidEmail(String email) throws Exception;

}
