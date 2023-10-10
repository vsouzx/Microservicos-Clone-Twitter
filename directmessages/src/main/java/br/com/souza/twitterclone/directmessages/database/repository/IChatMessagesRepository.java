package br.com.souza.twitterclone.directmessages.database.repository;

import br.com.souza.twitterclone.directmessages.database.model.ChatMessages;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatMessagesRepository extends JpaRepository<ChatMessages, String> {

    List<ChatMessages> findAllByChatIdentifier(String identifier);
}
