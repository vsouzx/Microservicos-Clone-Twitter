package br.com.souza.twitterclone.accounts.database.repository;

import br.com.souza.twitterclone.accounts.database.model.UsersSearchHistoric;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersSearchHistoricRepository extends JpaRepository<UsersSearchHistoric, String> {

    Optional<UsersSearchHistoric> findBySearcherIdentifierAndSearchedIdentifier(String searcherIdentifier, String searchedIdentifier);
    List<UsersSearchHistoric> findAllBySearcherIdentifier(String searcherIdentifier);
}
