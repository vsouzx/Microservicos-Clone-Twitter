package br.comsouza.twitterclone.feed.service.posts;

import org.springframework.web.multipart.MultipartFile;

public interface IPostsService {

    void postNewTweet(String message, String sessionIdIdentifier, MultipartFile attachment) throws Exception;
}
