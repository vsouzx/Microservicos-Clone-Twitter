package br.comsouza.twitterclone.feed.service.interactions;

import br.comsouza.twitterclone.feed.database.model.Tweets;
import br.comsouza.twitterclone.feed.database.model.TweetsFavs;
import br.comsouza.twitterclone.feed.database.model.TweetsLikes;
import br.comsouza.twitterclone.feed.database.model.TweetsViews;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface IInteractionsService {

    List<Tweets> getTweetComments(String tweetIdentifier);
    Page<Tweets> getTweetCommentsPageable(String tweetIdentifier, Integer page, Integer size);
    List<Tweets> getTweetRetweets(String tweetIdentifier);
    List<TweetsLikes> getTweetLikes(String tweetIdentifier);
    List<TweetsViews> getTweetViews(String tweetIdentifier);
    List<TweetsFavs> getTweetFavs(String tweetIdentifier);
    void increaseViewsCount(String tweetIdentifier, String userIdentifier);
    Optional<TweetsLikes> verifyIsLiked(String tweetIdentifier, String userIdentifier);
    Optional<Tweets> verifyIsRetweeted(String tweetIdentifier, String userIdentifier);
    Optional<Tweets> verifyIsCommented(String tweetIdentifier, String userIdentifier);
    Optional<TweetsFavs> verifyIsFavorited(String tweetIdentifier, String userIdentifier);
}
