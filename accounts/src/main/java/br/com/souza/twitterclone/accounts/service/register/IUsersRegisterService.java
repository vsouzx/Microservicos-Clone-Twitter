package br.com.souza.twitterclone.accounts.service.register;

import br.com.souza.twitterclone.accounts.dto.user.UserRegistrationRequest;

public interface IUsersRegisterService {

    void userRegister(UserRegistrationRequest request) throws Exception;
    void sendConfirmationCode(String email) throws Exception;
    void confirmCode(String email, String code) throws Exception;

}
