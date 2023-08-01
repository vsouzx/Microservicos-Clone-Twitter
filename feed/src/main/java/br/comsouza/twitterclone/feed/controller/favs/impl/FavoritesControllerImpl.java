package br.comsouza.twitterclone.feed.controller.favs.impl;

import br.comsouza.twitterclone.feed.controller.favs.IFavoritesController;
import br.comsouza.twitterclone.feed.dto.favs.FavTweetResponse;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.favs.IFavoritesService;
import br.comsouza.twitterclone.feed.util.FindUserIdentifierHelper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/favorites")
public class FavoritesControllerImpl implements IFavoritesController {

    private final IFavoritesService iFavoritesService;

    public FavoritesControllerImpl(IFavoritesService iFavoritesService) {
        this.iFavoritesService = iFavoritesService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FavTweetResponse>> getFavsTweets(@RequestParam(value = "page", required = true) Integer page,
                                                                @RequestParam(value = "size", required = true) Integer size){
        return new ResponseEntity<>(iFavoritesService.getFavsTweets(FindUserIdentifierHelper.getIdentifier(), page, size), HttpStatus.OK);
    }
}
