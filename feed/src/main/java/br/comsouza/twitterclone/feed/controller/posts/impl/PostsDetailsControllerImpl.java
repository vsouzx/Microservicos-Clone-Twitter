package br.comsouza.twitterclone.feed.controller.posts.impl;

import br.comsouza.twitterclone.feed.controller.posts.IPostsDetailsController;
import br.comsouza.twitterclone.feed.dto.client.UserDetailsByIdentifierResponse;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.posts.IPostsDetailsService;
import br.comsouza.twitterclone.feed.service.redis.RedisService;
import br.comsouza.twitterclone.feed.util.FindUserIdentifierHelper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/posts/detail")
public class PostsDetailsControllerImpl implements IPostsDetailsController {

    private final IPostsDetailsService iPostsDetailsService;
    private final RedisService redisService;

    public PostsDetailsControllerImpl(IPostsDetailsService iPostsDetailsService,
                                      RedisService redisService) {
        this.iPostsDetailsService = iPostsDetailsService;
        this.redisService = redisService;
    }

    @GetMapping(value = "/{tweetIdentifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimelineTweetResponse> getTweetDetails(@PathVariable("tweetIdentifier") String tweetIdentifier,
                                                                 @RequestParam(value = "load", required = false) Boolean load) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        return new ResponseEntity<>(iPostsDetailsService.getTweetDetails(sessionUserIdentifier, tweetIdentifier, load), HttpStatus.OK);
    }

    @GetMapping(value = "/comments/{tweetIdentifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TimelineTweetResponse>> getTweetComments(@PathVariable("tweetIdentifier") String tweetIdentifier,
                                                                        @RequestParam(value = "page", required = true) Integer page,
                                                                        @RequestParam(value = "size", required = true) Integer size) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        return new ResponseEntity<>(iPostsDetailsService.getTweetComments(sessionUserIdentifier, tweetIdentifier, page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/novalueretweets/{tweetIdentifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDetailsByIdentifierResponse>> getTweetNoValueRetweets(@PathVariable("tweetIdentifier") String tweetIdentifier,
                                                                                         @RequestParam(value = "page", required = true) Integer page,
                                                                                         @RequestParam(value = "size", required = true) Integer size) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        return new ResponseEntity<>(iPostsDetailsService.getTweetNoValueRetweets(tweetIdentifier, page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/retweets/{tweetIdentifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TimelineTweetResponse>> getTweetRetweets(@PathVariable("tweetIdentifier") String tweetIdentifier,
                                                                        @RequestParam(value = "page", required = true) Integer page,
                                                                        @RequestParam(value = "size", required = true) Integer size) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        return new ResponseEntity<>(iPostsDetailsService.getTweetRetweets(sessionUserIdentifier, tweetIdentifier, page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/likes/{tweetIdentifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDetailsByIdentifierResponse>> getTweetLikes(@PathVariable("tweetIdentifier") String tweetIdentifier,
                                                                               @RequestParam(value = "page", required = true) Integer page,
                                                                               @RequestParam(value = "size", required = true) Integer size) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        return new ResponseEntity<>(iPostsDetailsService.getTweetLikes(tweetIdentifier, page, size), HttpStatus.OK);
    }

}
