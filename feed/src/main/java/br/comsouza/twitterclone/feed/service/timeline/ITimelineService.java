package br.comsouza.twitterclone.feed.service.timeline;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import java.util.List;

public interface ITimelineService {

    List<TimelineTweetResponse> getFollowingTimeline(String sessionUserIdentifier, Integer page, Integer size) throws Exception;
    List<TimelineTweetResponse> getForyouTimeline(String sessionUserIdentifier, Integer page, Integer size) throws Exception;
}
