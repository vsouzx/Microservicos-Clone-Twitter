package br.com.souza.twitterclone.accounts.database.repository.impl;

import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import br.com.souza.twitterclone.accounts.service.interactions.IUsersInteractionsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class UsersRepositoryImpl {

    @PersistenceContext
    private final EntityManager em;

    public UsersRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    public List<UserDetailsByIdentifierResponse> findAllByUsername(String sessionUserIdentifier, String targetUsername, Integer page, Integer size, String authorization){
        StringBuilder sb = new StringBuilder();

        sb.append("DECLARE @sessionUser	        VARCHAR(MAX)    = ? ");
        sb.append("	      ,@targetUsername		VARCHAR(MAX)    = ? ");
        sb.append("	      ,@PageNumber			INT             = ? ");
        sb.append("       ,@RowsOfPage			INT             = ? ");
        sb.append("   ");
        sb.append("SELECT DISTINCT  u.identifier  ");
        sb.append("				,u.first_name    ");
        sb.append("			    ,u.username    ");
        sb.append("				,u.biography    ");
        sb.append("				,u.location  ");
        sb.append("				,u.site  ");
        sb.append("				,u.registration_time  ");
        sb.append("				,u.private_account    ");
        sb.append("				,u.profile_photo_url  ");
        sb.append("				,u.background_photo_url  ");
        sb.append("				,u.verified  ");
        sb.append("				,(SELECT IIF(MAX(blocker_identifier) IS NULL, 0, 1)  ");
        sb.append("				  FROM blocked_users ");
        sb.append("				  WHERE blocked_identifier = u.identifier ");
        sb.append("					AND blocker_identifier = @sessionUser) isBlockedByMe ");
        sb.append(" ");
        sb.append("				,(SELECT IIF(MAX(blocked_identifier) IS NULL, 0, 1)  ");
        sb.append("				  FROM blocked_users ");
        sb.append("				  WHERE blocked_identifier = @sessionUser ");
        sb.append("					AND blocker_identifier = u.identifier) hasBlockedMe ");
        sb.append(" ");
        sb.append("				,(SELECT IIF(MAX(follower_identifier) IS NULL, 0, 1)  ");
        sb.append("				  FROM users_follows ");
        sb.append("				  WHERE followed_identifier = u.identifier ");
        sb.append("					AND follower_identifier = @sessionUser) isFollowedByMe ");
        sb.append(" ");
        sb.append("				,(SELECT IIF(MAX(pending_follower_identifier) IS NULL, 0, 1)  ");
        sb.append("				  FROM users_pending_follows ");
        sb.append("				  WHERE pending_followed_identifier = u.identifier ");
        sb.append("					AND pending_follower_identifier = @sessionUser) isPendingFollowedByMe ");
        sb.append(" ");
        sb.append("				,(SELECT IIF(MAX(followed_identifier) IS NULL, 0, 1)  ");
        sb.append("				  FROM users_follows ");
        sb.append("				  WHERE followed_identifier = @sessionUser ");
        sb.append("					AND follower_identifier = u.identifier) isFollowingMe ");
        sb.append("FROM users u     ");
        sb.append("WHERE (UPPER(u.username) LIKE UPPER('%'+ @targetUsername + '%') OR UPPER(u.first_name) LIKE UPPER('%'+ @targetUsername + '%'))  ");
        sb.append("	AND u.identifier <> @sessionUser  ");
        sb.append("ORDER BY u.identifier   ");
        sb.append("OFFSET (@PageNumber) * @RowsOfPage ROWS   ");
        sb.append("FETCH NEXT @RowsOfPage ROWS ONLY   ");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, sessionUserIdentifier);
        query.setParameter(2, targetUsername);
        query.setParameter(3, page);
        query.setParameter(4, size);

        List<Object[]> lista = query.getResultList();

        List<UserDetailsByIdentifierResponse> response = new ArrayList<>();

        for (Object[] result : lista) {
            response.add(UserDetailsByIdentifierResponse.builder()
                    .userIdentifier((String) result[0])
                    .firstName((String) result[1])
                    .username((String) result[2])
                    .biography((String) result[3])
                    .location((String) result[4])
                    .site((String) result[5])
                    .registrationTime(((Timestamp) result[6]).toLocalDateTime())
                    .privateAccount((Boolean) result[7])
                    .profilePhotoUrl((String) result[8])
                    .isVerified((Boolean) result[10])
                    .isBlockedByMe((Boolean) result[11])
                    .hasBlockedMe((Boolean) result[12])
                    .isFollowedByMe((Boolean) result[13])
                    .isPendingFollowedByMe((Boolean) result[14])
                    .isFollowingMe((Boolean) result[15])
                    .build());
        }
        return response;
    }

    public List<UserPreviewResponse> getUserPendingFollowers(String sessionUserIdentifier, Integer page, Integer size) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("DECLARE @sessionUser	            VARCHAR(MAX) = ?  ");
        sb.append("	      ,@PageNumber				INT = ?  ");
        sb.append("       ,@RowsOfPage				INT = ?     ");
        sb.append(" ");
        sb.append("SELECT DISTINCT  u.first_name   ");
        sb.append("			    ,u.username   ");
        sb.append("				,u.biography   ");
        sb.append("				,u.private_account   ");
        sb.append("				,IIF(uf.followed_identifier IS NOT NULL, 1, 0)	isFollowedBySessionUser   ");
        sb.append("				,u.profile_photo_url   ");
        sb.append("FROM users u     ");
        sb.append("INNER JOIN users_pending_follows f     ");
        sb.append("	ON f.pending_followed_identifier = @sessionUser ");
        sb.append("	AND f.pending_follower_identifier = u.identifier  ");
        sb.append("LEFT JOIN users_follows uf    ");
        sb.append("	ON uf.followed_identifier = f.pending_follower_identifier ");
        sb.append("	AND uf.follower_identifier = f.pending_followed_identifier  ");
        sb.append("WHERE u.identifier <> @sessionUser   ");
        sb.append("ORDER BY isFollowedBySessionUser   ");
        sb.append("OFFSET (@PageNumber - 1) * @RowsOfPage ROWS   ");
        sb.append("FETCH NEXT @RowsOfPage ROWS ONLY   ");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, sessionUserIdentifier);
        query.setParameter(2, page);
        query.setParameter(3, size);

        List<Object[]> lista = query.getResultList();

        List<UserPreviewResponse> response = new ArrayList<>();

        for (Object[] result : lista) {
            response.add(UserPreviewResponse.builder()
                    .firstName((String) result[0])
                    .username((String) result[1])
                    .biography((String) result[2])
                    .privateAccount((Boolean) result[3])
                    .isFollowedByMe((Boolean) result[4])
                    .isFollowingMe((Boolean) result[5])
                    .profilePhotoUrl((String) result[6])
                    .build());
        }
        return response;
    }
}
