package br.com.souza.twitterclone.accounts.service.search;

import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserEmailUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserInfosUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPasswordUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPrivacyUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserUsernameUpdateRequest;

public interface IUsersSearchService {

    UserDetailsResponse searchUserInfos(String userIdentifier) throws Exception;

    UserDetailsByIdentifierResponse searchUserInfosByIdentifier(String userIdentifier, String targetUserIdentifier) throws Exception;

}
