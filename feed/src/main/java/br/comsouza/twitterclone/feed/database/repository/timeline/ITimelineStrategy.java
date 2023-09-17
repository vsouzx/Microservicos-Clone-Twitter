package br.comsouza.twitterclone.feed.database.repository.timeline;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import java.util.List;

public interface ITimelineStrategy {

    List<TimelineTweetResponse> getTimeLine(String sessionUserIdentifier, Integer page, Integer size, String authorization);
}
