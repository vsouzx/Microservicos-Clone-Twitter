package br.comsouza.twitterclone.feed.database.repository.explore;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import java.util.List;

public interface IExploreStrategy {

    List<TimelineTweetResponse> find(String keyword, Integer page, Integer size, String authorization);
    String getStrategyName();
}
