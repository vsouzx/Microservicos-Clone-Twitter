package br.comsouza.twitterclone.feed.service.posts;

public interface IPostsDetailsService {

    void getTweetDetails(String sessionUserIdentifier, String tweetIdentifier) throws Exception;
}
