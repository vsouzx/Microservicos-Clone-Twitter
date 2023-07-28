package br.comsouza.twitterclone.feed.service.tweettype;

import br.comsouza.twitterclone.feed.database.model.TweetsTypes;

public interface ITweetTypeService {

    TweetsTypes findTweetTypeByDescription(String description);
}
