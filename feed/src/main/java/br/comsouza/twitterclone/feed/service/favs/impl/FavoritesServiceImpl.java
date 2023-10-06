package br.comsouza.twitterclone.feed.service.favs.impl;

import br.comsouza.twitterclone.feed.database.repository.favs.FavoriteTweetsRepository;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.favs.IFavoritesService;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FavoritesServiceImpl implements IFavoritesService {

    private final FavoriteTweetsRepository favoriteTweetsRepository;
    private final IPostsService iPostsService;

    public FavoritesServiceImpl(FavoriteTweetsRepository favoriteTweetsRepository,
                                IPostsService iPostsService) {
        this.favoriteTweetsRepository = favoriteTweetsRepository;
        this.iPostsService = iPostsService;
    }

    @Override
    public List<TimelineTweetResponse> getFavsTweets(String userIdentifier, Integer page, Integer size, String authorization) throws Exception {
        List<TimelineTweetResponse> favs = favoriteTweetsRepository.find(userIdentifier, page, size);

        for(TimelineTweetResponse fav : favs){
            iPostsService.loadTweetResponses(fav, userIdentifier, authorization);
        }
        return favs;
    }

}
