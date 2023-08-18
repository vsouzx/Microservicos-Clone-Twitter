package br.comsouza.twitterclone.feed.database.repository;

import br.comsouza.twitterclone.feed.database.model.Tweets;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITweetsRepository extends JpaRepository<Tweets, String> {

    List<Tweets> findAllByOriginalTweetIdentifierAndTypeIn(String originalTweetId, List<String> typeIdentifier);
    Page<Tweets> findAllByOriginalTweetIdentifierAndTypeIn(String originalTweetId, List<String> typeIdentifier, Pageable pageable);
    Page<Tweets> findAllByOriginalTweetIdentifierAndTypeInOrderByPublicationTimeDesc(String originalTweetId, List<String> typeIdentifier, @Nullable Pageable pageable);

    Optional<Tweets> findByUserIdentifierAndOriginalTweetIdentifierAndType(String sessionUserIdentifier, String originalTweetIdentifier, String typeIdentifier);

}
