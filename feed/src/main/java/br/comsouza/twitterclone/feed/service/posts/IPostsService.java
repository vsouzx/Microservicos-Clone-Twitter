package br.comsouza.twitterclone.feed.service.posts;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IPostsService {

    void postNewTweet(String message, String sessionUserIdentifier, MultipartFile attachment, String flag) throws Exception;
    void retweetToggle(String message, String sessionUserIdentifier, MultipartFile attachment, String originalTweetIdentifier, String authorization) throws Exception;
    void commentToggle(String message, String sessionUserIdentifier, MultipartFile attachment, String originalTweetIdentifier, String identifier) throws Exception;
    void likeToggle(String tweetIdentifier, String sessionUserIdentifier, String authorization) throws Exception;
    void favToggle(String tweetIdentifier, String sessionUserIdentifier, String authorization) throws Exception;
    TimelineTweetResponse getPostResumeByIdentifier(TimelineTweetResponse firstTweet, TimelineTweetResponse secondTweet, String sessionUserIdentifier, boolean isThirdLevel) throws Exception;
}
