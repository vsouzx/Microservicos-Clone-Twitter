package br.com.souza.twitterclone.accounts.database.repository.followsdetails;

import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import java.util.List;

public interface IFollowsDetailsStrategy {

    List<UserPreviewResponse> getUserFollowsInformations(String sessionUserIdentifier, String targetUserIdentifier, Integer page, Integer size);
}
