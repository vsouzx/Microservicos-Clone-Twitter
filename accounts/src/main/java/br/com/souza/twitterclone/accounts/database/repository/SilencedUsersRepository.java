package br.com.souza.twitterclone.accounts.database.repository;

import br.com.souza.twitterclone.accounts.database.model.SilencedUsers;
import br.com.souza.twitterclone.accounts.database.model.SilencedUsersId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SilencedUsersRepository extends JpaRepository<SilencedUsers, SilencedUsersId> {

}
