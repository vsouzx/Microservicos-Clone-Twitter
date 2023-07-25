package br.com.souza.twitterclone.accounts.service.register;

import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserEmailUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserInfosUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPasswordUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserPrivacyUpdateRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserRegistrationRequest;
import br.com.souza.twitterclone.accounts.dto.user.UserUsernameUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface IUsersRegisterService {

    void userRegister(String request, MultipartFile file) throws Exception;
    void resendConfirmationCode(String email) throws Exception;

}
