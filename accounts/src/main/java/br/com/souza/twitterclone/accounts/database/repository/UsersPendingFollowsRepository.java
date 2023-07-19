package br.com.souza.twitterclone.accounts.database.repository;

import br.com.souza.twitterclone.accounts.database.model.UsersPendingFollows;
import br.com.souza.twitterclone.accounts.database.model.UsersPendingFollowsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersPendingFollowsRepository extends JpaRepository<UsersPendingFollows, UsersPendingFollowsId> {

}
