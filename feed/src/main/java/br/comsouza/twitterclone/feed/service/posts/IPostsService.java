package br.comsouza.twitterclone.feed.service.posts;

import org.springframework.web.multipart.MultipartFile;

public interface IPostsService {

    void postNewTweet(String message, String sessionUserIdentifier, MultipartFile attachment) throws Exception;
    void retweetToggle(String message, String sessionUserIdentifier, MultipartFile attachment, String originalTweetIdentifier) throws Exception;
    void commentToggle(String message, String sessionUserIdentifier, MultipartFile attachment, String originalTweetIdentifier) throws Exception;
    void likeToggle(String tweetIdentifier, String sessionUserIdentifier) throws Exception;
    void favToggle(String tweetIdentifier, String sessionUserIdentifier) throws Exception;
}
