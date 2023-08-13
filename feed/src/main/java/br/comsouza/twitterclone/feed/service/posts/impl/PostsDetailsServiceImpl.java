package br.comsouza.twitterclone.feed.service.posts.impl;

import br.comsouza.twitterclone.feed.database.model.Tweets;
import br.comsouza.twitterclone.feed.database.repository.postdetails.PostDetailsRepository;
import br.comsouza.twitterclone.feed.database.repository.timeline.PostResumeRepository;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
import br.comsouza.twitterclone.feed.service.posts.IPostsDetailsService;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PostsDetailsServiceImpl implements IPostsDetailsService {

    private final PostDetailsRepository postDetailsRepository;
    private final IInteractionsService iInteractionsService;
    private final PostResumeRepository postResumeRepository;
    private final IPostsService iPostsService;

    public PostsDetailsServiceImpl(PostDetailsRepository postDetailsRepository,
                                   IInteractionsService iInteractionsService,
                                   PostResumeRepository postResumeRepository,
                                   IPostsService iPostsService) {
        this.postDetailsRepository = postDetailsRepository;
        this.iInteractionsService = iInteractionsService;
        this.postResumeRepository = postResumeRepository;
        this.iPostsService = iPostsService;
    }

    @Override
    public TimelineTweetResponse getTweetDetails(String sessionUserIdentifier, String tweetIdentifier) throws Exception {
        TimelineTweetResponse post = postDetailsRepository.find(sessionUserIdentifier, tweetIdentifier);
        post.setOriginalTweetResponse(iPostsService.getPostResumeByIdentifier(post, post, sessionUserIdentifier, false));

        TimelineTweetResponse originalTweet = post.getOriginalTweetResponse();
        if(originalTweet != null){
            originalTweet.setOriginalTweetResponse(iPostsService.getPostResumeByIdentifier(post, originalTweet, sessionUserIdentifier, true));
        }

        post.setTweetCommentsList(getTweetComments(sessionUserIdentifier, tweetIdentifier, 0, 10));

        return post;
    }

    @Override
    public List<TimelineTweetResponse> getTweetComments(String sessionUserIdentifier, String tweetIdentifier, Integer page, Integer size) throws Exception {
        List<TimelineTweetResponse> response = new ArrayList<>();
        Page<Tweets> comments = iInteractionsService.getTweetCommentsPageable(tweetIdentifier, page, size);
        for(Tweets comment : comments.getContent()){
            response.add(postResumeRepository.find(sessionUserIdentifier, comment.getTweetIdentifier()));
        }
        return response;
    }

}
