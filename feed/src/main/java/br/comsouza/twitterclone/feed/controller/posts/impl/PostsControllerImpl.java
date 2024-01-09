package br.comsouza.twitterclone.feed.controller.posts.impl;

import br.comsouza.twitterclone.feed.controller.posts.IPostsController;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import br.comsouza.twitterclone.feed.service.redis.RedisService;
import br.comsouza.twitterclone.feed.util.FindUserIdentifierHelper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/v1/posts")
public class PostsControllerImpl implements IPostsController {

    private final IPostsService iPostsService;
    private final RedisService redisService;

    public PostsControllerImpl(IPostsService iPostsService,
                               RedisService redisService) {
        this.iPostsService = iPostsService;
        this.redisService = redisService;
    }

    @PostMapping(value = "/newtweet")
    public ResponseEntity<Void> postNewTweet(@RequestPart(value = "message", required = false) String request,
                                             @RequestPart(value = "canBeReplied", required = true) Integer flag,
                                             @RequestPart(value = "attachment", required = false) List<MultipartFile> attachments) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        iPostsService.postNewTweet(request, sessionUserIdentifier, attachments, flag);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/retweettoggle/{originalTweet}")
    public ResponseEntity<Void> retweetToggle(@PathVariable(value = "originalTweet") String originalTweet,
                                              @RequestPart(value = "message", required = false) String request,
                                              @RequestPart(value = "canBeReplied", required = true) Integer flag,
                                              @RequestPart(value = "attachment", required = false) List<MultipartFile> attachments) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        iPostsService.retweetToggle(request, sessionUserIdentifier, attachments, originalTweet, flag);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/newcomment/{originalTweet}")
    public ResponseEntity<Void> commentToggle(@PathVariable(value = "originalTweet") String originalTweet,
                                              @RequestPart(value = "message", required = false) String request,
                                              @RequestPart(value = "attachment", required = false) List<MultipartFile> attachments) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        iPostsService.commentToggle(request, sessionUserIdentifier, attachments, originalTweet);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/liketoggle/{tweet}")
    public ResponseEntity<Void> likeToggle(@PathVariable(value = "tweet") String tweet) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        iPostsService.likeToggle(tweet, sessionUserIdentifier);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/favtoggle/{tweet}")
    public ResponseEntity<Void> favToggle(@PathVariable(value = "tweet") String tweet) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        iPostsService.favToggle(tweet, sessionUserIdentifier);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/count/{userIdentifier}")
    public ResponseEntity<Integer> getTweetsCount(@PathVariable(value = "userIdentifier") String userIdentifier) throws Exception{
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        return new ResponseEntity<>(iPostsService.getTweetsCount(userIdentifier), HttpStatus.CREATED);
    }
}
