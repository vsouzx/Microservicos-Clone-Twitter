package br.comsouza.twitterclone.feed.service.posts.impl;

import br.comsouza.twitterclone.feed.client.IAccountsClient;
import br.comsouza.twitterclone.feed.database.repository.postdetails.PostDetailsRepository;
import br.comsouza.twitterclone.feed.database.repository.postdetails.PostResumeRepository;
import br.comsouza.twitterclone.feed.dto.client.UserDetailsByIdentifierResponse;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
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
    public TimelineTweetResponse getTweetDetails(String sessionUserIdentifier, String tweetIdentifier, String authorization) throws Exception {
        TimelineTweetResponse post = postDetailsRepository.find(sessionUserIdentifier, tweetIdentifier, authorization);
        iPostsService.loadTweetResponses(post, sessionUserIdentifier, authorization);
        post.setTweetCommentsList(getTweetComments(sessionUserIdentifier, tweetIdentifier, 0, 10, authorization));

        return post;
    }

    @Override
    public List<TimelineTweetResponse> getTweetComments(String sessionUserIdentifier, String tweetIdentifier, Integer page, Integer size, String authorization){
        List<TimelineTweetResponse> response = new ArrayList<>();
        iInteractionsService.getTweetCommentsPageable(tweetIdentifier, page, size).getContent()
                .forEach(comment -> {
                    response.add(postResumeRepository.find(sessionUserIdentifier, comment.getTweetIdentifier(), authorization));
                });
        return response;
    }

    @Override
    public List<UserDetailsByIdentifierResponse> getTweetNoValueRetweets(String authorization, String tweetIdentifier, Integer page, Integer size){

        List<UserDetailsByIdentifierResponse> response = new ArrayList<>();
        iInteractionsService.getTweetOnlyNoValueRetweetsPageable(tweetIdentifier, page, size).getContent()
                .forEach(retweet -> {
                    response.add(iAccountsClient.getUserInfosByIdentifier(retweet.getUserIdentifier(), authorization));
                });
        return response;
    }

    @Override
    public List<TimelineTweetResponse> getTweetRetweets(String sessionUserIdentifier, String tweetIdentifier, Integer page, Integer size, String authorization){

        List<TimelineTweetResponse> response = new ArrayList<>();
        iInteractionsService.getTweetOnlyValuedRetweetsPageable(tweetIdentifier, page, size).getContent()
                .forEach(retweet -> {
                    response.add(postResumeRepository.find(sessionUserIdentifier, retweet.getTweetIdentifier(), authorization));
                });

        for(TimelineTweetResponse post : response){
            iPostsService.loadTweetResponses(post, sessionUserIdentifier, authorization);
        }

        return response;
    }

    @Override
    public List<UserDetailsByIdentifierResponse> getTweetLikes(String authorization, String tweetIdentifier, Integer page, Integer size){

        List<UserDetailsByIdentifierResponse> response = new ArrayList<>();
        iInteractionsService.getTweetLikesPageable(tweetIdentifier, page, size).getContent()
                .forEach(like -> {
                    response.add(iAccountsClient.getUserInfosByIdentifier(like.getId().getUserIdentifier(), authorization));
                });
        return response;
    }

}
