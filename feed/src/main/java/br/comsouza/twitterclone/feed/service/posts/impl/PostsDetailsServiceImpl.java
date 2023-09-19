package br.comsouza.twitterclone.feed.service.posts.impl;

import br.comsouza.twitterclone.feed.client.IAccountsClient;
import br.comsouza.twitterclone.feed.database.model.Tweets;
import br.comsouza.twitterclone.feed.database.model.TweetsLikes;
import br.comsouza.twitterclone.feed.database.repository.ITweetsRepository;
import br.comsouza.twitterclone.feed.database.repository.postdetails.PostDetailsRepository;
import br.comsouza.twitterclone.feed.database.repository.postdetails.PostResumeRepository;
import br.comsouza.twitterclone.feed.dto.client.UserDetailsByIdentifierResponse;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.handler.exceptions.ServerSideErrorException;
import br.comsouza.twitterclone.feed.handler.exceptions.TweetNotFoundException;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
import br.comsouza.twitterclone.feed.service.posts.IPostsDetailsService;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PostsDetailsServiceImpl implements IPostsDetailsService {

    private final PostDetailsRepository postDetailsRepository;
    private final IInteractionsService iInteractionsService;
    private final PostResumeRepository postResumeRepository;
    private final IPostsService iPostsService;
    private final IAccountsClient iAccountsClient;
    private final ITweetsRepository iTweetsRepository;

    public PostsDetailsServiceImpl(PostDetailsRepository postDetailsRepository,
                                   IInteractionsService iInteractionsService,
                                   PostResumeRepository postResumeRepository,
                                   IPostsService iPostsService,
                                   IAccountsClient iAccountsClient,
                                   ITweetsRepository iTweetsRepository) {
        this.postDetailsRepository = postDetailsRepository;
        this.iInteractionsService = iInteractionsService;
        this.postResumeRepository = postResumeRepository;
        this.iPostsService = iPostsService;
        this.iAccountsClient = iAccountsClient;
        this.iTweetsRepository = iTweetsRepository;
    }

    @Override
    public TimelineTweetResponse getTweetDetails(String sessionUserIdentifier, String tweetIdentifier, String authorization, Boolean load) throws Exception {
        iTweetsRepository.findById(tweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        TimelineTweetResponse tweet = postDetailsRepository.find(sessionUserIdentifier, tweetIdentifier, authorization);

        if(load != null && !load){
            return tweet;
        }

        iPostsService.loadTweetResponses(tweet, sessionUserIdentifier, authorization);
        tweet.setTweetCommentsList(getTweetComments(sessionUserIdentifier, tweetIdentifier, 0, 10, authorization));

        return tweet;
    }

    @Override
    public List<TimelineTweetResponse> getTweetComments(String sessionUserIdentifier, String tweetIdentifier, Integer page, Integer size, String authorization) throws TweetNotFoundException {
        iTweetsRepository.findById(tweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        List<TimelineTweetResponse> response = new ArrayList<>();
        List<Tweets> comments = iInteractionsService.getTweetCommentsPageable(tweetIdentifier, page, size).getContent();
        for (Tweets comment : comments) {
            response.add(postResumeRepository.find(sessionUserIdentifier, comment.getTweetIdentifier(), authorization));
        }
        return response;
    }

    @Override
    public List<UserDetailsByIdentifierResponse> getTweetNoValueRetweets(String authorization, String tweetIdentifier, Integer page, Integer size) throws Exception {
        iTweetsRepository.findById(tweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        List<UserDetailsByIdentifierResponse> response = new ArrayList<>();
        List<Tweets> retweets = iInteractionsService.getTweetOnlyNoValueRetweetsPageable(tweetIdentifier, page, size).getContent();
        for (Tweets retweet : retweets) {
            response.add(iAccountsClient.getUserInfosByIdentifier(retweet.getUserIdentifier(), authorization));
        }
        return response;
    }

    @Override
    public List<TimelineTweetResponse> getTweetRetweets(String sessionUserIdentifier, String tweetIdentifier, Integer page, Integer size, String authorization) throws Exception {
        iTweetsRepository.findById(tweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        List<TimelineTweetResponse> response = new ArrayList<>();
        List<Tweets> retweets = iInteractionsService.getTweetOnlyValuedRetweetsPageable(tweetIdentifier, page, size).getContent();
        for (Tweets retweet : retweets) {
            response.add(postResumeRepository.find(sessionUserIdentifier, retweet.getTweetIdentifier(), authorization));
        }

        for (TimelineTweetResponse post : response) {
            iPostsService.loadTweetResponses(post, sessionUserIdentifier, authorization);
        }
        return response;
    }

    @Override
    public List<UserDetailsByIdentifierResponse> getTweetLikes(String authorization, String tweetIdentifier, Integer page, Integer size) throws Exception {
        iTweetsRepository.findById(tweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        List<UserDetailsByIdentifierResponse> response = new ArrayList<>();
        List<TweetsLikes> likes =  iInteractionsService.getTweetLikesPageable(tweetIdentifier, page, size).getContent();

        for (TweetsLikes like : likes) {
            response.add(iAccountsClient.getUserInfosByIdentifier(like.getId().getUserIdentifier(), authorization));
        }
        return response;
    }

}
