package br.comsouza.twitterclone.feed.service.explorer;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import java.util.List;

public interface IExplorerService {

    List<TimelineTweetResponse> find(String type, String keyword, Integer page, Integer size, String sessionUserIdentifier) throws Exception;
}
