package br.com.souza.twitterclone.directmessages.database.repository.impl;

import br.com.souza.twitterclone.directmessages.dto.ChatsResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class LoadDirectMessagesRepositoryImpl {

    @PersistenceContext
    private final EntityManager entityManager;

    public LoadDirectMessagesRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<ChatsResponse> getAllChats(String sessionUserIdentifier) {
        List<ChatsResponse> response = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @sessionUserIdentifier UNIQUEIDENTIFIER = ? ; ");
        sb.append(" ");
        sb.append("WITH LatestMessages AS (  ");
        sb.append("    SELECT chat_identifier chat_identifier  ");
        sb.append("          ,MAX(creation_date) lastMessageDate  ");
        sb.append("    FROM chat_messages d  ");
        sb.append("	INNER JOIN dm_chats c ");
        sb.append("		ON c.identifier = d.chat_identifier ");
        sb.append("    WHERE (c.user_identifier_1 = @sessionUserIdentifier OR c.user_identifier_2 = @sessionUserIdentifier)  ");
        sb.append("    GROUP BY chat_identifier  ");
        sb.append(")  ");
        sb.append(" ");
        sb.append("SELECT c.identifier ");
        sb.append("	     ,u.identifier ");
        sb.append("	     ,u.first_name ");
        sb.append("	     ,u.username ");
        sb.append("	     ,u.verified ");
        sb.append("	     ,u.profile_photo_url ");
        sb.append("	     ,m.text ");
        sb.append("	     ,m.creation_date ");
        sb.append("	     ,m.seen ");
        sb.append("FROM LatestMessages l ");
        sb.append("INNER JOIN dm_chats c  ");
        sb.append("	ON c.identifier = l.chat_identifier ");
        sb.append("INNER JOIN chat_messages m ");
        sb.append("	ON m.chat_identifier = c.identifier ");
        sb.append("	AND m.creation_date = l.lastMessageDate ");
        sb.append("INNER JOIN users u ");
        sb.append("	ON u.identifier = IIF(c.user_identifier_1 = @sessionUserIdentifier, c.user_identifier_2, c.user_identifier_1) ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter(1, sessionUserIdentifier);

        List<Object[]> lista = query.getResultList();

        lista.forEach(result -> {
            response.add(ChatsResponse.builder()
                    .chatIdentifier((String) result[0])
                    .userIdentifier((String) result[1])
                    .userFirstName((String) result[2])
                    .userUsername((String) result[3])
                    .isUserVerified((Boolean) result[4])
                    .userProfilePhotoUrl((String) result[5])
                    .lastMessageText((String) result[6])
                    .lastMessageDate(((Timestamp) result[7]).toLocalDateTime())
                    .lastMessageSeen((Boolean) result[8])
                    .build());
        });

        return response;
    }
}
