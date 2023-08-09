package br.comsouza.twitterclone.feed.database.repository;

import br.comsouza.twitterclone.feed.database.model.TweetsFavs;
import br.comsouza.twitterclone.feed.database.model.TweetsFavsId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITweetsFavsRepository extends JpaRepository<TweetsFavs, TweetsFavsId> {

    Optional<TweetsFavs> findAllByIdTweetIdentifierAndIdUserIdentifier(String tweetIdentifier, String userIdentifier);

    List<TweetsFavs> findAllByIdTweetIdentifier(String tweetIdentifier);
}
