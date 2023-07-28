package br.comsouza.twitterclone.feed.service.timeline;

import br.comsouza.twitterclone.feed.dto.handler.posts.TimelineTweetResponse;
import java.util.List;

public interface ITimelineService {

    List<TimelineTweetResponse> getFollowingTimeline(String sessionUserIdentifier);
}
