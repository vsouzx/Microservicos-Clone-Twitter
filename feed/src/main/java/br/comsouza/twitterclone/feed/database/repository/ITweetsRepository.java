package br.comsouza.twitterclone.feed.database.repository;

import br.comsouza.twitterclone.feed.database.model.Tweets;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITweetsRepository extends JpaRepository<Tweets, String> {

    List<Tweets> findAllByOriginalTweetIdentifierAndType(String originalTweetId, String typeIdentifier);

    Optional<Tweets> findByUserIdentifierAndOriginalTweetIdentifierAndType(String sessionUserIdentifier, String originalTweetIdentifier, String typeIdentifier);

}
