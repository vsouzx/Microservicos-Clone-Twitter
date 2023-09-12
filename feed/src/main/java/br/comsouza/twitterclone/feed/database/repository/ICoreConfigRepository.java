package br.comsouza.twitterclone.feed.database.repository;

import br.comsouza.twitterclone.feed.database.model.CoreConfig;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICoreConfigRepository extends JpaRepository<CoreConfig, String> {

    Optional<CoreConfig> findByKeyName(String keyName);
}
