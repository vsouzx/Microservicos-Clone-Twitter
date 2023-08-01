package br.comsouza.twitterclone.feed.service.favs;

import br.comsouza.twitterclone.feed.dto.favs.FavTweetResponse;
import java.util.List;

public interface IFavoritesService {

    List<FavTweetResponse> getFavsTweets(String userIdentifier,Integer page,Integer size);

}
