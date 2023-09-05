package br.comsouza.twitterclone.feed.service.posts;

import br.comsouza.twitterclone.feed.dto.client.UserDetailsByIdentifierResponse;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import java.util.List;

public interface IPostsDetailsService {

    TimelineTweetResponse getTweetDetails(String sessionUserIdentifier, String tweetIdentifier) throws Exception;
    List<TimelineTweetResponse> getTweetComments(String sessionUserIdentifier, String tweetIdentifier, Integer page, Integer size) throws Exception;
    List<UserDetailsByIdentifierResponse> getTweetNoValueRetweets(String authorization, String tweetIdentifier, Integer page, Integer size) throws Exception;
    List<TimelineTweetResponse> getTweetRetweets(String authorization, String tweetIdentifier, Integer page, Integer size);
    List<UserDetailsByIdentifierResponse> getTweetLikes(String authorization, String tweetIdentifier, Integer page, Integer size) throws Exception;
}
