package br.com.souza.twitterclone.accounts.database.repository.impl;

import br.com.souza.twitterclone.accounts.dto.user.KnownUsersResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.service.interactions.IUsersInteractionsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AllUserKnownFollowersRepository{

    @PersistenceContext
    private final EntityManager em;

    public AllUserKnownFollowersRepository(EntityManager em) {
        this.em = em;
    }

    public List<KnownUsersResponse> getUserFollowsInformations(String sessionUserIdentifier, String targetUserIdentifier){
        StringBuilder sb = new StringBuilder();

        sb.append("DECLARE @sessionUser	    VARCHAR(MAX) = ?  ");
        sb.append("	      ,@targetUser		VARCHAR(MAX) = ?  ");
        sb.append("   ");
        sb.append("SELECT DISTINCT u.first_name    ");
        sb.append("				  ,u.profile_photo_url  ");
        sb.append("FROM users u     ");
        sb.append("INNER JOIN users_follows f     ");
        sb.append("	ON f.followed_identifier = @targetUser    ");
        sb.append("	AND f.follower_identifier = u.identifier  ");
        sb.append("LEFT JOIN users_follows common     ");
        sb.append("	ON common.followed_identifier = f.follower_identifier  ");
        sb.append("	AND common.follower_identifier = @sessionUser  ");
        sb.append("WHERE common.follower_identifier IS NOT NULL ");
        sb.append("ORDER BY u.first_name   ");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, sessionUserIdentifier);
        query.setParameter(2, targetUserIdentifier);

        List<Object[]> lista = query.getResultList();

        List<KnownUsersResponse> response = new ArrayList<>();

        for (Object[] result : lista) {
            response.add(KnownUsersResponse.builder()
                    .firstName((String) result[0])
                    .profilePhotoUrl((String) result[1])
                    .build());
        }

        return response;
    }
}
