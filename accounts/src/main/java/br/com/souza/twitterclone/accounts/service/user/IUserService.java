package br.com.souza.twitterclone.accounts.service.user;

import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;

public interface IUserService {

    User findUserByUsernameOrEmailOrIdentifier(String targetUserIdentifier) throws UserNotFoundException;
}
