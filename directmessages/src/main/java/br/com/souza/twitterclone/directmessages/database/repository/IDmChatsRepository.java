package br.com.souza.twitterclone.directmessages.database.repository;

import br.com.souza.twitterclone.directmessages.database.model.DmChats;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDmChatsRepository extends JpaRepository<DmChats, String> {

    Optional<DmChats> findByUserIdentifier1AndUserIdentifier2(String userIdentifier1, String userIdentifier2);
    List<DmChats> findAllByUserIdentifier1(String userIdentifier1);

}
