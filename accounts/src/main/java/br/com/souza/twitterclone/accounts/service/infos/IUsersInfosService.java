package br.com.souza.twitterclone.accounts.service.infos;

import br.com.souza.twitterclone.accounts.dto.user.UserEmailUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserInfosUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPasswordUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPrivacyUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserUsernameUpdateRequest;

public interface IUsersInfosService {

    void updateUserInfos(UserInfosUpdateRequest request, String identifier) throws Exception;

    void updateUserEmail(UserEmailUpdateRequest request, String identifier, String authorization) throws Exception;

    void updateUserUsername(UserUsernameUpdateRequest request, String identifier) throws Exception;

    void updateUserPassword(UserPasswordUpdateRequest request, String identifier) throws Exception;

    void updateUserPrivacy(UserPrivacyUpdateRequest request, String identifier) throws Exception;
}
