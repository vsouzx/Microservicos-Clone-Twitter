package br.com.souza.twitterclone.accounts.database.repository.followsdetails;

import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import java.util.List;

public interface IFollowsDetailsStrategy {

    List<UserDetailsByIdentifierResponse> getUserFollowsInformations(String sessionUserIdentifier, String targetUserIdentifier, Integer page, Integer size, String authorization);
    String getStrategyName();
}
