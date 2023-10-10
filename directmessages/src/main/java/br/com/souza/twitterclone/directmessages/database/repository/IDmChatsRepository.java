package br.com.souza.twitterclone.directmessages.database.repository;

import br.com.souza.twitterclone.directmessages.database.model.DmChats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDmChatsRepository extends JpaRepository<DmChats, String> {

}
