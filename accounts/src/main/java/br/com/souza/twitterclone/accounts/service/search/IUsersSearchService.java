package br.com.souza.twitterclone.accounts.service.search;

import br.com.souza.twitterclone.accounts.dto.user.*;

import java.util.List;

public interface IUsersSearchService {

    UserDetailsResponse searchUserInfos(String userIdentifier, String authorization) throws Exception;
    UserDetailsByIdentifierResponse searchUserInfosByIdentifier(String userIdentifier, String targetUserIdentifier, String authorization) throws Exception;
    List<UserPreviewResponse> getUsersByUsername(String sessionUserIdentifier, String targetUserIdentifier, Integer page, Integer size) throws Exception;
    List<UserPreviewResponse> getUserFollowers(String sessionUserIdentifier, String targetUsername, Integer page, Integer size) throws Exception;
    List<UserPreviewResponse> getUserFollows(String sessionUserIdentifier, String targetUsername, Integer page, Integer size) throws Exception;
    List<UserPreviewResponse> getUserPendingFollowers(String sessionUserIdentifier, Integer page, Integer size) throws Exception;
    ValidEmailResponse isValidEmail(String email);
    ValidUsernameResponse isValidUsername(String username);
    ValidUserResponse isValidUser(String username);
    List<UserDetailsByIdentifierResponse> getWhoToFollow(String sessionUserIdentifier, Integer page, Integer size, String userOnScreen, String authorization) throws Exception;
    List<UserPreviewResponse> getVerified();
    List<String> getAlertedUsers(String sessionUserIdentifier) throws Exception;
    List<UserPreviewResponse> getCommonFollows(String sessionUserIdentifier, String targetUserIdentifier) throws Exception;
}
