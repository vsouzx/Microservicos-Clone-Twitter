package br.com.souza.twitterclone.accounts.database.repository;

import br.com.souza.twitterclone.accounts.database.model.AlertedUsers;
import br.com.souza.twitterclone.accounts.database.model.AlertedUsersId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertedUsersRepository extends JpaRepository<AlertedUsers, AlertedUsersId> {

    List<AlertedUsers> findAllByIdAlertedIdentifier(String identifier);
}
