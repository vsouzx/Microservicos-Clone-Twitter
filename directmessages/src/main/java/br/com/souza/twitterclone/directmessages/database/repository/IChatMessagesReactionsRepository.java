package br.com.souza.twitterclone.directmessages.database.repository;

import br.com.souza.twitterclone.directmessages.database.model.ChatMessagesReactions;
import br.com.souza.twitterclone.directmessages.database.model.ChatMessagesReactionsId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatMessagesReactionsRepository extends JpaRepository<ChatMessagesReactions, ChatMessagesReactionsId> {

    List<ChatMessagesReactions> findAllByIdMessageIdentifier(String messageIdentifier);
    Optional<ChatMessagesReactions> findByIdUserIdentifierAndIdMessageIdentifier(String userIdentifier, String messageIdentifier);
}
