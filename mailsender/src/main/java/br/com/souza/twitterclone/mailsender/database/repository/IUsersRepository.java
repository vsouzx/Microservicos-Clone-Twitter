package br.com.souza.twitterclone.mailsender.database.repository;

import br.com.souza.twitterclone.mailsender.database.model.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsersRepository extends JpaRepository<Users, String> {

    Optional<Users> findByEmail(String email);
}
