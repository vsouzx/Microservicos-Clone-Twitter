package br.comsouza.twitterclone.feed.service.interactions;

import br.comsouza.twitterclone.feed.database.model.Tweets;
import br.comsouza.twitterclone.feed.database.model.TweetsFavs;
import br.comsouza.twitterclone.feed.database.model.TweetsLikes;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface IInteractionsService {

    Page<Tweets> getTweetCommentsPageable(String tweetIdentifier, Integer page, Integer size);
    Page<Tweets> getTweetOnlyNoValueRetweetsPageable(String tweetIdentifier, Integer page, Integer size);
    Page<Tweets> getTweetOnlyValuedRetweetsPageable(String tweetIdentifier, Integer page, Integer size);
    Page<TweetsLikes> getTweetLikesPageable(String tweetIdentifier, Integer page, Integer size);
    void increaseViewsCount(String tweetIdentifier, String userIdentifier);
    Optional<TweetsLikes> verifyIsLiked(String tweetIdentifier, String userIdentifier);
    Optional<Tweets> verifyIsRetweeted(String tweetIdentifier, String userIdentifier);
    Optional<Tweets> verifyIsCommented(String tweetIdentifier, String userIdentifier);
    Optional<TweetsFavs> verifyIsFavorited(String tweetIdentifier, String userIdentifier);
}
