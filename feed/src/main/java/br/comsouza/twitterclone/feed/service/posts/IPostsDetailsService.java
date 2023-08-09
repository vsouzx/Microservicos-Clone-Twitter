package br.comsouza.twitterclone.feed.service.posts;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;

public interface IPostsDetailsService {

    TimelineTweetResponse getTweetDetails(String sessionUserIdentifier, String tweetIdentifier) throws Exception;
}
