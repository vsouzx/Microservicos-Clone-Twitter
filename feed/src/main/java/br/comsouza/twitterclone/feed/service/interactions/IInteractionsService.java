package br.comsouza.twitterclone.feed.service.interactions;

import br.comsouza.twitterclone.feed.database.model.Tweets;
import br.comsouza.twitterclone.feed.database.model.TweetsFavs;
import br.comsouza.twitterclone.feed.database.model.TweetsLikes;
import br.comsouza.twitterclone.feed.database.model.TweetsViews;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface IInteractionsService {

    List<Tweets> getAllTweetComments(String tweetIdentifier);
    Page<Tweets> getTweetCommentsPageable(String tweetIdentifier, Integer page, Integer size);
    List<Tweets> getTweetAllRetweetsTypes(String tweetIdentifier);
    Page<Tweets> getTweetOnlyNoValueRetweetsPageable(String tweetIdentifier, Integer page, Integer size);
    Page<Tweets> getTweetOnlyValuedRetweetsPageable(String tweetIdentifier, Integer page, Integer size);
    List<TweetsLikes> getTweetLikes(String tweetIdentifier);
    Page<TweetsLikes> getTweetLikesPageable(String tweetIdentifier, Integer page, Integer size);
    List<TweetsViews> getTweetViews(String tweetIdentifier);
    List<TweetsFavs> getTweetFavs(String tweetIdentifier);
    void increaseViewsCount(String tweetIdentifier, String userIdentifier);
    Optional<TweetsLikes> verifyIsLiked(String tweetIdentifier, String userIdentifier);
    Optional<Tweets> verifyIsRetweeted(String tweetIdentifier, String userIdentifier);
    Optional<Tweets> verifyIsCommented(String tweetIdentifier, String userIdentifier);
    Optional<TweetsFavs> verifyIsFavorited(String tweetIdentifier, String userIdentifier);
}
