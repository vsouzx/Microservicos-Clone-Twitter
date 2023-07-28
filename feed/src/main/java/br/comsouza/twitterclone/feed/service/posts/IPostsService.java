package br.comsouza.twitterclone.feed.service.posts;

import org.springframework.web.multipart.MultipartFile;

public interface IPostsService {

    void postNewTweet(String message, String sessionIdIdentifier, MultipartFile attachment) throws Exception;
    void retweetToggle(String message, String sessionIdIdentifier, MultipartFile attachment, String originalTweetIdentifier) throws Exception;
    void commentToggle(String message, String sessionIdIdentifier, MultipartFile attachment, String originalTweetIdentifier) throws Exception;
}
