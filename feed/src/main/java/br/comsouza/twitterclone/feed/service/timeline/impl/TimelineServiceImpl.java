package br.comsouza.twitterclone.feed.service.timeline.impl;

import br.comsouza.twitterclone.feed.database.repository.timeline.FollowingTimelineRepository;
import br.comsouza.twitterclone.feed.database.repository.timeline.ForyouTimelineRepository;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import br.comsouza.twitterclone.feed.service.timeline.ITimelineService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TimelineServiceImpl implements ITimelineService {

    private final FollowingTimelineRepository followingTimelineRepository;
    private final ForyouTimelineRepository foryouTimelineRepository;
    private final IPostsService iPostsService;

    public TimelineServiceImpl(FollowingTimelineRepository followingTimelineRepository,
                               ForyouTimelineRepository foryouTimelineRepository,
                               IPostsService iPostsService) {
        this.followingTimelineRepository = followingTimelineRepository;
        this.foryouTimelineRepository = foryouTimelineRepository;
        this.iPostsService = iPostsService;
    }

    @Override
    public List<TimelineTweetResponse> getFollowingTimeline(String sessionUserIdentifier, Integer page, Integer size) throws Exception{
        List<TimelineTweetResponse> posts = followingTimelineRepository.find(sessionUserIdentifier, page, size);

        for(TimelineTweetResponse post : posts){
            iPostsService.loadTweetResponses(post, sessionUserIdentifier);
        }
        return posts;
    }

    @Override
    public List<TimelineTweetResponse> getForyouTimeline(String sessionUserIdentifier, Integer page, Integer size) throws Exception {
        List<TimelineTweetResponse> posts = foryouTimelineRepository.find(sessionUserIdentifier, page, size);

        for(TimelineTweetResponse post : posts){
            iPostsService.loadTweetResponses(post, sessionUserIdentifier);
        }
        return posts;
    }

}
