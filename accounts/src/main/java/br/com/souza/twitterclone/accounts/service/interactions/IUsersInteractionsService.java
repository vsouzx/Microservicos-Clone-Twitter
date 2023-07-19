package br.com.souza.twitterclone.accounts.service.interactions;

import br.com.souza.twitterclone.accounts.dto.user.UserEmailUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserInfosUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPasswordUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPrivacyUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserUsernameUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface IUsersInteractionsService {

    void blockToggle(String sessionUserIdentifier, String targetIdentifier) throws Exception;

}
