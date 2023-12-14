package br.comsouza.twitterclone.feed.controller.favs.impl;

import br.comsouza.twitterclone.feed.controller.favs.IFavoritesController;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.favs.IFavoritesService;
import br.comsouza.twitterclone.feed.service.redis.RedisService;
import br.comsouza.twitterclone.feed.util.FindUserIdentifierHelper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/favorites")
public class FavoritesControllerImpl implements IFavoritesController {

    private final IFavoritesService iFavoritesService;
    private final RedisService redisService;

    public FavoritesControllerImpl(IFavoritesService iFavoritesService,
                                   RedisService redisService) {
        this.iFavoritesService = iFavoritesService;
        this.redisService = redisService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TimelineTweetResponse>> getFavsTweets(@RequestParam(value = "page", required = true) Integer page,
                                                                     @RequestParam(value = "size", required = true) Integer size) throws Exception {
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        return new ResponseEntity<>(iFavoritesService.getFavsTweets(sessionUserIdentifier, page, size), HttpStatus.OK);
    }
}
