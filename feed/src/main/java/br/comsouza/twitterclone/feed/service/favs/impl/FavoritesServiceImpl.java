package br.comsouza.twitterclone.feed.service.favs.impl;

import br.comsouza.twitterclone.feed.database.repository.favs.FavoriteTweetsRepository;
import br.comsouza.twitterclone.feed.dto.favs.FavTweetResponse;
import br.comsouza.twitterclone.feed.service.favs.IFavoritesService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FavoritesServiceImpl implements IFavoritesService {

    private final FavoriteTweetsRepository favoriteTweetsRepository;

    public FavoritesServiceImpl(FavoriteTweetsRepository favoriteTweetsRepository) {
        this.favoriteTweetsRepository = favoriteTweetsRepository;
    }

    @Override
    public List<FavTweetResponse> getFavsTweets(String userIdentifier, Integer page, Integer size) {
        return favoriteTweetsRepository.find(userIdentifier, page, size);
    }

}
