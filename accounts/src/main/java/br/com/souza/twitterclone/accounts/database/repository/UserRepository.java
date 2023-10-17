package br.com.souza.twitterclone.accounts.database.repository;

import br.com.souza.twitterclone.accounts.database.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT DISTINCT  u.* " +
            "FROM users u " +
            "INNER JOIN users_follows f " +
            " ON f.followed_identifier = :userIdentifier " +
            " AND f.follower_identifier = u.identifier", nativeQuery = true)
    List<User> findUserFollowers(@Param("userIdentifier") String userIdentifier);

    @Query(value = "SELECT DISTINCT  u.* " +
                   "FROM users u " +
                   "INNER JOIN users_follows f " +
                   "  ON f.followed_identifier = u.identifier " +
                   "  AND f.follower_identifier = :userIdentifier ", nativeQuery = true)
    List<User> findUserFollows(@Param("userIdentifier") String userIdentifier);

    List<User> findAllByVerified(Boolean verified);
}
