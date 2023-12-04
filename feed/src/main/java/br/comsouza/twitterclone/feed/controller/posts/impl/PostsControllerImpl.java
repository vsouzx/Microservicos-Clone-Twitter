package br.comsouza.twitterclone.feed.controller.posts.impl;

import br.comsouza.twitterclone.feed.controller.posts.IPostsController;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import br.comsouza.twitterclone.feed.util.FindUserIdentifierHelper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/v1/posts")
public class PostsControllerImpl implements IPostsController {

    private final IPostsService iPostsService;

    public PostsControllerImpl(IPostsService iPostsService) {
        this.iPostsService = iPostsService;
    }

    @PostMapping(value = "/newtweet")
    public ResponseEntity<Void> postNewTweet(@RequestPart(value = "message", required = false) String request,
                                             @RequestPart(value = "canBeReplied", required = true) String flag,
                                             @RequestPart(value = "attachment", required = false) List<MultipartFile> attachments,
                                             @RequestHeader("Authorization") String authorization) throws Exception {
        iPostsService.postNewTweet(request, FindUserIdentifierHelper.getIdentifier(), attachments, flag, authorization);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/retweettoggle/{originalTweet}")
    public ResponseEntity<Void> retweetToggle(@PathVariable(value = "originalTweet") String originalTweet,
                                              @RequestPart(value = "message", required = false) String request,
                                              @RequestPart(value = "attachment", required = false) List<MultipartFile> attachments,
                                              @RequestHeader("Authorization") String authorization) throws Exception {
        iPostsService.retweetToggle(request, FindUserIdentifierHelper.getIdentifier(), attachments, originalTweet, authorization);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/newcomment/{originalTweet}")
    public ResponseEntity<Void> commentToggle(@PathVariable(value = "originalTweet") String originalTweet,
                                              @RequestPart(value = "message", required = false) String request,
                                              @RequestPart(value = "attachment", required = false) List<MultipartFile> attachments,
                                              @RequestHeader("Authorization") String authorization) throws Exception {
        iPostsService.commentToggle(request, FindUserIdentifierHelper.getIdentifier(), attachments, originalTweet, authorization);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/liketoggle/{tweet}")
    public ResponseEntity<Void> likeToggle(@PathVariable(value = "tweet") String tweet,
                                           @RequestHeader("Authorization") String authorization) throws Exception {
        iPostsService.likeToggle(tweet, FindUserIdentifierHelper.getIdentifier(), authorization);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/favtoggle/{tweet}")
    public ResponseEntity<Void> favToggle(@PathVariable(value = "tweet") String tweet,
                                          @RequestHeader("Authorization") String authorization) throws Exception {
        iPostsService.favToggle(tweet, FindUserIdentifierHelper.getIdentifier(), authorization);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/count/{userIdentifier}")
    public ResponseEntity<Integer> getTweetsCount(@PathVariable(value = "userIdentifier") String userIdentifier){
        return new ResponseEntity<>(iPostsService.getTweetsCount(userIdentifier), HttpStatus.CREATED);
    }
}
