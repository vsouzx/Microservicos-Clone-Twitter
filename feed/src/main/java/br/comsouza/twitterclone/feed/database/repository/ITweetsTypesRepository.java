package br.comsouza.twitterclone.feed.database.repository;

import br.comsouza.twitterclone.feed.database.model.TweetsTypes;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITweetsTypesRepository extends JpaRepository<TweetsTypes, String> {

    Optional<TweetsTypes> findByDescription(String description);
}
