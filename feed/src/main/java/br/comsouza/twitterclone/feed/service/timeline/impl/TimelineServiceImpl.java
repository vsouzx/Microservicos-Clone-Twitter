package br.comsouza.twitterclone.feed.service.timeline.impl;

import br.comsouza.twitterclone.feed.database.repository.timeline.ITimelineStrategy;
import br.comsouza.twitterclone.feed.database.repository.timeline.factory.FollowingTimelineStrategyFactory;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import br.comsouza.twitterclone.feed.service.timeline.ITimelineService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TimelineServiceImpl implements ITimelineService {

    private final IPostsService iPostsService;
    private final FollowingTimelineStrategyFactory followingTimelineStrategyFactory;

    public TimelineServiceImpl(IPostsService iPostsService,
                               FollowingTimelineStrategyFactory followingTimelineStrategyFactory) {

        this.iPostsService = iPostsService;
        this.followingTimelineStrategyFactory = followingTimelineStrategyFactory;
    }

    @Override
    public List<TimelineTweetResponse> getTimeline(String sessionUserIdentifier, Integer page, Integer size, String type) throws Exception{
        ITimelineStrategy strategy = followingTimelineStrategyFactory.getStrategy(type);
        List<TimelineTweetResponse> posts = strategy.getTimeLine(sessionUserIdentifier, page, size);

        for(TimelineTweetResponse post : posts){
            iPostsService.loadTweetResponses(post, sessionUserIdentifier);
        }
        
        return posts;
    }

}
