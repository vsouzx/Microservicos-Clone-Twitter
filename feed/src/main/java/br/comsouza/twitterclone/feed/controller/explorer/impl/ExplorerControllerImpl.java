package br.comsouza.twitterclone.feed.controller.explorer.impl;

import br.comsouza.twitterclone.feed.controller.explorer.IExplorerController;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.explorer.IExplorerService;
import br.comsouza.twitterclone.feed.service.redis.RedisService;
import br.comsouza.twitterclone.feed.util.FindUserIdentifierHelper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/explorer")
public class ExplorerControllerImpl implements IExplorerController {

    private final IExplorerService iExplorerService;
    private final RedisService redisService;

    public ExplorerControllerImpl(IExplorerService iExplorerService,
                                  RedisService redisService) {
        this.iExplorerService = iExplorerService;
        this.redisService = redisService;
    }

    @GetMapping(value = "/{type}")
    public ResponseEntity<List<TimelineTweetResponse>> getExploredTweets(@PathVariable(value = "type") String type,
                                                                         @RequestParam(value = "keyword", required = false) String keyword,
                                                                         @RequestParam(value = "page") Integer page,
                                                                         @RequestParam(value = "size") Integer size) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        return new ResponseEntity<>(iExplorerService.find(type, keyword, page, size, sessionUserIdentifier), HttpStatus.OK);
    }
}
