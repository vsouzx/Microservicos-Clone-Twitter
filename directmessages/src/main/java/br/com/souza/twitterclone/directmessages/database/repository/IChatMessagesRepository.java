package br.com.souza.twitterclone.directmessages.database.repository;

import br.com.souza.twitterclone.directmessages.database.model.ChatMessages;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatMessagesRepository extends JpaRepository<ChatMessages, String> {

    List<ChatMessages> findAllByChatIdentifier(String identifier);
    @Query(value = "DECLARE @chatIdentifier UNIQUEIDENTIFIER = :chatIdentifier " +
                   "DECLARE @sessionUserIdentifier UNIQUEIDENTIFIER = :userIdentifier " +
                   "DECLARE @page INT = :page " +
                   "DECLARE @size INT = :size " +
                   "SELECT m.*  " +
                   "FROM chat_messages m " +
                   "LEFT JOIN chat_ignored_messages i " +
                   "  ON i.chat_identifier = @chatIdentifier " +
                   "  AND i.message_identifier  = m.identifier    " +
                   "  AND i.user_identifier     = @sessionUserIdentifier " +
                   "WHERE i.message_identifier IS NULL "+
                   "ORDER BY creation_date DESC " +
                   "OFFSET (@page) * @size ROWS   " +
                   "FETCH NEXT @size ROWS ONLY   ",
            nativeQuery = true)
    List<ChatMessages> findAllByChatIdentifier(@Param("chatIdentifier") String chatIdentifier,
                                               @Param("userIdentifier") String userIdentifier,
                                               @Param("page") Integer page,
                                               @Param("size") Integer size);
}
