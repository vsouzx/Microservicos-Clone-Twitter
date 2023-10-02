package br.com.souza.twitterclone.accounts.database.repository.impl;

import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
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

    public List<UserPreviewResponse> findAllByUsername(String sessionUserIdentifier, String targetUsername, Integer page, Integer size) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("DECLARE @sessionUser	        VARCHAR(MAX) = ? ");
        sb.append("	      ,@targetUsername		VARCHAR(MAX) = ? ");
        sb.append("	      ,@PageNumber		    INT = ? ");
        sb.append("       ,@RowsOfPage		    INT = ?  ");
        sb.append(" ");
        sb.append("SELECT u.first_name ");
        sb.append("	  ,u.username ");
        sb.append("	  ,u.biography ");
        sb.append("	  ,u.private_account ");
        sb.append("   ,CONVERT(BIT, IIF(f.followed_identifier IS NOT NULL, 1, 0)) isFollowedBySessionUser ");
        sb.append("   ,CONVERT(BIT, IIF(f2.followed_identifier IS NOT NULL, 1, 0)) isFollowingSessionUser ");
        sb.append("	  ,u.profile_photo_url ");
        sb.append("FROM Users u  ");
        sb.append("LEFT JOIN users_follows f  ");
        sb.append("  ON f.follower_identifier = @sessionUser ");
        sb.append("  AND f.followed_identifier = u.identifier  ");
        sb.append("LEFT JOIN users_follows f2  ");
        sb.append("	ON f2.followed_identifier = @sessionUser  ");
        sb.append("	AND f2.follower_identifier = u.identifier ");
        sb.append("WHERE u.identifier <> @sessionUser ");
        sb.append("AND (@targetUsername IS NULL   ");
        sb.append("OR UPPER(username) LIKE UPPER('%'+ @targetUsername +'%'))  ");
        sb.append("ORDER BY isFollowedBySessionUser desc ");
        sb.append("	    ,username  ");
        sb.append("OFFSET (@PageNumber - 1) * @RowsOfPage ROWS ");
        sb.append("FETCH NEXT @RowsOfPage ROWS ONLY  ");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, sessionUserIdentifier);
        query.setParameter(2, targetUsername);
        query.setParameter(3, page);
        query.setParameter(4, size);

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

    public List<UserPreviewResponse> getFollowers(String sessionUserIdentifier, String targetUserIdentifier, Integer page, Integer size) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("DECLARE @sessionUser	        VARCHAR(MAX) = ? ");
        sb.append("	      ,@targetUser			VARCHAR(MAX) = ? ");
        sb.append("	      ,@PageNumber			INT = ? ");
        sb.append("       ,@RowsOfPage			INT = ?   ");
        sb.append("  ");
        sb.append("SELECT DISTINCT  u.first_name  ");
        sb.append("			    ,u.username  ");
        sb.append("				,u.biography  ");
        sb.append("				,u.private_account  ");
        sb.append("				,CONVERT(BIT, IIF(uf.followed_identifier IS NOT NULL, 1, 0))	isFollowedBySessionUser  ");
        sb.append("				,CONVERT(BIT, IIF(uf2.followed_identifier IS NOT NULL, 1, 0))	isFollowingSessionUser   ");
        sb.append("				,u.profile_photo_url  ");
        sb.append("FROM users u    ");
        sb.append("INNER JOIN users_follows f    ");
        sb.append("	ON f.followed_identifier = @targetUser   ");
        sb.append("	AND f.follower_identifier = u.identifier ");
        sb.append("LEFT JOIN users_follows uf   ");
        sb.append("	ON uf.followed_identifier = @sessionUser  ");
        sb.append("	AND uf.follower_identifier = f.follower_identifier  ");
        sb.append("LEFT JOIN users_follows uf2  ");
        sb.append("	ON uf2.followed_identifier = f.follower_identifier  ");
        sb.append("	AND uf2.follower_identifier = @sessionUser   ");
        sb.append("WHERE u.identifier <> @sessionUser  ");
        sb.append("ORDER BY isFollowedBySessionUser  ");
        sb.append("OFFSET (@PageNumber - 1) * @RowsOfPage ROWS  ");
        sb.append("FETCH NEXT @RowsOfPage ROWS ONLY  ");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, sessionUserIdentifier);
        query.setParameter(2, targetUserIdentifier);
        query.setParameter(3, page);
        query.setParameter(4, size);

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

    public List<UserPreviewResponse> getUserFollows(String sessionUserIdentifier, String targetUserIdentifier, Integer page, Integer size) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("DECLARE @sessionUser	        VARCHAR(MAX) = ? ");
        sb.append("	      ,@targetUser			VARCHAR(MAX) = ? ");
        sb.append("	      ,@PageNumber			INT = ?  ");
        sb.append("       ,@RowsOfPage			INT = ?    ");
        sb.append("  ");
        sb.append("SELECT DISTINCT  u.first_name  ");
        sb.append("			    ,u.username  ");
        sb.append("				,u.biography  ");
        sb.append("				,u.private_account  ");
        sb.append("				,IIF(uf.followed_identifier IS NOT NULL, 1, 0)	isFollowedBySessionUser  ");
        sb.append("				,IIF(uf2.followed_identifier IS NOT NULL, 1, 0) isFollowingSessionUser   ");
        sb.append("				,u.profile_photo_url  ");
        sb.append("FROM users u    ");
        sb.append("INNER JOIN users_follows f    ");
        sb.append("	ON f.followed_identifier = u.identifier    ");
        sb.append("	AND f.follower_identifier = @targetUser  ");
        sb.append("LEFT JOIN users_follows uf   ");
        sb.append("	ON uf.followed_identifier = f.followed_identifier  ");
        sb.append("	AND uf.follower_identifier = @sessionUser  ");
        sb.append("LEFT JOIN users_follows uf2  ");
        sb.append("	ON uf2.followed_identifier = @sessionUser   ");
        sb.append("	AND uf2.follower_identifier = f.followed_identifier  ");
        sb.append("WHERE u.identifier <> @sessionUser  ");
        sb.append("ORDER BY isFollowedBySessionUser  ");
        sb.append("OFFSET (@PageNumber - 1) * @RowsOfPage ROWS  ");
        sb.append("FETCH NEXT @RowsOfPage ROWS ONLY  ");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, sessionUserIdentifier);
        query.setParameter(2, targetUserIdentifier);
        query.setParameter(3, page);
        query.setParameter(4, size);

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
