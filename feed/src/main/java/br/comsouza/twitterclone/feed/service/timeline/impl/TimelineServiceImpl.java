package br.comsouza.twitterclone.feed.service.timeline.impl;

import br.comsouza.twitterclone.feed.database.repository.timeline.FollowingTimelineRepository;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.timeline.ITimelineService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TimelineServiceImpl implements ITimelineService {

    private final FollowingTimelineRepository followingTimelineRepository;

    public TimelineServiceImpl(FollowingTimelineRepository followingTimelineRepository) {
        this.followingTimelineRepository = followingTimelineRepository;
    }

    @Override
    public List<TimelineTweetResponse> getFollowingTimeline(String sessionUserIdentifier, Integer page, Integer size) {
        return followingTimelineRepository.find(sessionUserIdentifier, page, size);
    }

}
