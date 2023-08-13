package br.comsouza.twitterclone.feed.service.posts;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import java.util.List;

public interface IPostsDetailsService {

    TimelineTweetResponse getTweetDetails(String sessionUserIdentifier, String tweetIdentifier) throws Exception;
    List<TimelineTweetResponse> getTweetComments(String sessionUserIdentifier, String tweetIdentifier, Integer page, Integer size) throws Exception;
}
