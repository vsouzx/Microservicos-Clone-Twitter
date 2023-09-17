package br.comsouza.twitterclone.feed.service.timeline;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import java.util.List;

public interface ITimelineService {

    List<TimelineTweetResponse> getTimeline(String sessionUserIdentifier, Integer page, Integer size, String type, String authorization) throws Exception;
}
