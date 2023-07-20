package br.com.souza.twitterclone.accounts.database.repository;

import br.com.souza.twitterclone.accounts.database.model.UsersFollows;
import br.com.souza.twitterclone.accounts.database.model.UsersFollowsId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersFollowsRepository extends JpaRepository<UsersFollows, UsersFollowsId> {

}
