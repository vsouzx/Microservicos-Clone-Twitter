package br.comsouza.twitterclone.feed.controller.posts.impl;

import br.comsouza.twitterclone.feed.controller.posts.IPostsController;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import br.comsouza.twitterclone.feed.util.FindUserIdentifierHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public PostsControllerImpl(IPostsService iPostsService) {
        this.iPostsService = iPostsService;
    }

    @PostMapping(value = "/newtweet", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> postNewTweet(@RequestPart(value = "message", required = false) String request,
                                             @RequestPart(value = "attachment", required = false) MultipartFile attachment) throws Exception {
        iPostsService.postNewTweet(request, FindUserIdentifierHelper.getIdentifier(), attachment);

        //TODO: implementar lógica de buscar tradução da mensagens (se tiver) no Chat GPT

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/retweettoggle/{originalTweet}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> retweetToggle(@PathVariable(value = "originalTweet") String originalTweet,
                                              @RequestPart(value = "message", required = false) String request,
                                              @RequestPart(value = "attachment", required = false) MultipartFile attachment) throws Exception {
        iPostsService.retweetToggle(request, FindUserIdentifierHelper.getIdentifier(), attachment, originalTweet);

        //TODO: implementar lógica de buscar tradução da mensagens (se tiver) no Chat GPT

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/commenttoggle/{originalTweet}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> commentToggle(@PathVariable(value = "originalTweet") String originalTweet,
                                              @RequestPart(value = "message", required = false) String request,
                                              @RequestPart(value = "attachment", required = false) MultipartFile attachment) throws Exception {
        iPostsService.commentToggle(request, FindUserIdentifierHelper.getIdentifier(), attachment, originalTweet);

        //TODO: implementar lógica de buscar tradução da mensagens (se tiver) no Chat GPT

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/liketoggle/{tweet}")
    public ResponseEntity<Void> likeToggle(@PathVariable(value = "tweet") String tweet) throws Exception {
        iPostsService.likeToggle(tweet, FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/favtoggle/{tweet}")
    public ResponseEntity<Void> favToggle(@PathVariable(value = "tweet") String tweet) throws Exception {
        iPostsService.favToggle(tweet, FindUserIdentifierHelper.getIdentifier());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
