package br.comsouza.twitterclone.feed.service.favs;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import java.util.List;

public interface IFavoritesService {

    List<TimelineTweetResponse> getFavsTweets(String userIdentifier, Integer page, Integer size, String authorization) throws Exception;

}
