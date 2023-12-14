package br.comsouza.twitterclone.feed.service.posts;

import br.comsouza.twitterclone.feed.dto.client.UserDetailsByIdentifierResponse;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.handler.exceptions.TweetNotFoundException;
import java.util.List;

public interface IPostsDetailsService {

    TimelineTweetResponse getTweetDetails(String sessionUserIdentifier, String tweetIdentifier, Boolean load) throws Exception;
    List<TimelineTweetResponse> getTweetComments(String sessionUserIdentifier, String tweetIdentifier, Integer page, Integer size) throws Exception;
    List<UserDetailsByIdentifierResponse> getTweetNoValueRetweets(String tweetIdentifier, Integer page, Integer size) throws Exception;
    List<TimelineTweetResponse> getTweetRetweets(String sessionUserIdentifier, String tweetIdentifier, Integer page, Integer size) throws Exception;
    List<UserDetailsByIdentifierResponse> getTweetLikes(String tweetIdentifier, Integer page, Integer size) throws Exception;
}
