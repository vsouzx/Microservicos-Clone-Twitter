package br.comsouza.twitterclone.feed.service.posts.impl;

import br.comsouza.twitterclone.feed.client.IAccountsClient;
import br.comsouza.twitterclone.feed.database.model.Tweets;
import br.comsouza.twitterclone.feed.database.model.TweetsLikes;
import br.comsouza.twitterclone.feed.database.repository.postdetails.PostDetailsRepository;
import br.comsouza.twitterclone.feed.database.repository.timeline.PostResumeRepository;
import br.comsouza.twitterclone.feed.dto.client.UserDetailsByIdentifierResponse;
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
    private final IAccountsClient iAccountsClient;

    public PostsDetailsServiceImpl(PostDetailsRepository postDetailsRepository,
                                   IInteractionsService iInteractionsService,
                                   PostResumeRepository postResumeRepository,
                                   IPostsService iPostsService,
                                   IAccountsClient iAccountsClient) {
        this.postDetailsRepository = postDetailsRepository;
        this.iInteractionsService = iInteractionsService;
        this.postResumeRepository = postResumeRepository;
        this.iPostsService = iPostsService;
        this.iAccountsClient = iAccountsClient;
    }

    @Override
    public TimelineTweetResponse getTweetDetails(String sessionUserIdentifier, String tweetIdentifier) throws Exception {
        TimelineTweetResponse post = postDetailsRepository.find(sessionUserIdentifier, tweetIdentifier);
        iPostsService.loadTweetResponses(post, sessionUserIdentifier);
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

    @Override
    public List<UserDetailsByIdentifierResponse> getTweetNoValueRetweets(String authorization, String tweetIdentifier, Integer page, Integer size) throws Exception {

        List<UserDetailsByIdentifierResponse> response = new ArrayList<>();
        Page<Tweets> retweets = iInteractionsService.getTweetOnlyNoValueRetweetsPageable(tweetIdentifier, page, size);

        for (Tweets retweet : retweets.getContent()){
            response.add(iAccountsClient.getUserInfosByIdentifier(retweet.getUserIdentifier(), authorization));
        }
        return response;
    }

    @Override
    public List<TimelineTweetResponse> getTweetRetweets(String sessionUserIdentifier, String tweetIdentifier, Integer page, Integer size) throws Exception {

        List<TimelineTweetResponse> response = new ArrayList<>();
        Page<Tweets> retweets = iInteractionsService.getTweetOnlyValuedRetweetsPageable(tweetIdentifier, page, size);

        for (Tweets retweet : retweets.getContent()){
            response.add(postResumeRepository.find(sessionUserIdentifier, retweet.getTweetIdentifier()));
        }

        for(TimelineTweetResponse post : response){
            iPostsService.loadTweetResponses(post, sessionUserIdentifier);
        }

        return response;
    }

    @Override
    public List<UserDetailsByIdentifierResponse> getTweetLikes(String authorization, String tweetIdentifier, Integer page, Integer size) throws Exception {

        List<UserDetailsByIdentifierResponse> response = new ArrayList<>();
        Page<TweetsLikes> likes = iInteractionsService.getTweetLikesPageable(tweetIdentifier, page, size);

        for (TweetsLikes like : likes){
            response.add(iAccountsClient.getUserInfosByIdentifier(like.getId().getUserIdentifier(), authorization));
        }
        return response;
    }

}
