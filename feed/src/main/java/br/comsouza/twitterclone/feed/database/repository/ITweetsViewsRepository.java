package br.comsouza.twitterclone.feed.database.repository;

import br.comsouza.twitterclone.feed.database.model.TweetsViews;
import br.comsouza.twitterclone.feed.database.model.TweetsViewsId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITweetsViewsRepository extends JpaRepository<TweetsViews, TweetsViewsId> {

    List<TweetsViews> findAllByIdTweetIdentifier(String tweetIdentifier);
}
