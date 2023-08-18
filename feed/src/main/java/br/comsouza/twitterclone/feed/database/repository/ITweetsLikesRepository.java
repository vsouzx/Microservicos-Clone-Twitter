package br.comsouza.twitterclone.feed.database.repository;

import br.comsouza.twitterclone.feed.database.model.TweetsLikes;
import br.comsouza.twitterclone.feed.database.model.TweetsLikesId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITweetsLikesRepository extends JpaRepository<TweetsLikes, TweetsLikesId> {

    List<TweetsLikes> findAllByIdTweetIdentifier(String tweetIdentifier);
    Page<TweetsLikes> findAllByIdTweetIdentifier(String tweetIdentifier, Pageable pageable);
    Optional<TweetsLikes> findAllByIdTweetIdentifierAndIdUserIdentifier(String tweetIdentifier, String userIdentifier);
}
