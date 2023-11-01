package br.com.souza.twitterclone.accounts.service.search;

import br.com.souza.twitterclone.accounts.database.model.UsersSearchHistoric;
import br.com.souza.twitterclone.accounts.dto.user.*;

import java.util.List;

public interface IUsersSearchService {

    UserDetailsResponse searchUserInfos(String userIdentifier, String authorization) throws Exception;
    UserDetailsByIdentifierResponse searchUserInfosByIdentifier(String userIdentifier, String targetUserIdentifier, String authorization) throws Exception;
    List<UserDetailsByIdentifierResponse> getUsersByUsername(String sessionUserIdentifier, String targetUserIdentifier, Integer page, Integer size, String authorization) throws Exception;
    List<UserDetailsByIdentifierResponse> getUserFollowsDetails(String sessionUserIdentifier, String targetUserIdentifier, String type, Integer page, Integer size, String authorization) throws Exception;
    List<UserPreviewResponse> getUserPendingFollowers(String sessionUserIdentifier, Integer page, Integer size) throws Exception;
    ValidEmailResponse isValidEmail(String email);
    ValidUsernameResponse isValidUsername(String username);
    ValidUserResponse isValidUser(String username);
    List<UserDetailsByIdentifierResponse> getWhoToFollow(String sessionUserIdentifier, Integer page, Integer size, String userOnScreen, Boolean isVerified, String authorization) throws Exception;
    List<UserPreviewResponse> getVerified();
    List<String> getAlertedUsers(String sessionUserIdentifier) throws Exception;
    FollowsAndFollowersResponse getFollowsAndFollowers(String targetUserIdentifier);
    List<KnownUsersResponse> getAllKnownFollowers(String sessionUserIdentifier, String targetUserIdentifier, String authorization) throws Exception;
    List<UserSearchHistoricResponse> getUserSearchHistoric(String sessionUserIdentifier, String authorization) throws Exception;
    void saveUserSearchHistoric(String sessionUserIdentifier, String targetUserIdentifier, String text) throws Exception;

}
