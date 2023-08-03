package br.comsouza.twitterclone.feed.service.timeline.impl;

import br.comsouza.twitterclone.feed.database.repository.timeline.FollowingTimelineRepository;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import br.comsouza.twitterclone.feed.service.timeline.ITimelineService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TimelineServiceImpl implements ITimelineService {

    private final FollowingTimelineRepository followingTimelineRepository;
    private final IPostsService iPostsService;

    public TimelineServiceImpl(FollowingTimelineRepository followingTimelineRepository,
                               IPostsService iPostsService) {
        this.followingTimelineRepository = followingTimelineRepository;
        this.iPostsService = iPostsService;
    }

    @Override
    public List<TimelineTweetResponse> getFollowingTimeline(String sessionUserIdentifier, Integer page, Integer size) throws Exception{
        List<TimelineTweetResponse> posts = followingTimelineRepository.find(sessionUserIdentifier, page, size);

        for(TimelineTweetResponse post : posts){
            post.setOriginalTweetResponse(iPostsService.getPostResumeByIdentifier(post, post, sessionUserIdentifier, false));

            if(post.getOriginalTweetResponse() != null){
                post.getOriginalTweetResponse().setOriginalTweetResponse(iPostsService.getPostResumeByIdentifier(post, post.getOriginalTweetResponse(), sessionUserIdentifier, true));
            }
        }
        return posts;
    }

}
