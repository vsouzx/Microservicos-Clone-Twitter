package br.com.souza.twitterclone.directmessages.database.repository;

import br.com.souza.twitterclone.directmessages.database.model.ChatIgnoredMessages;
import br.com.souza.twitterclone.directmessages.database.model.ChatIgnoredMessagesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatIgnoredMessagesRepository extends JpaRepository<ChatIgnoredMessages, ChatIgnoredMessagesId> {

}
