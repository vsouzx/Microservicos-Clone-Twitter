package br.com.souza.twitterclone.accounts.database.repository.impl;

import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class WhoToFollowRepositoryImpl {

    @PersistenceContext
    private final EntityManager em;

    public WhoToFollowRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    public List<UserPreviewResponse> find(String sessionUserIdentifier, Integer page, Integer size, String userOnScreen) {
        StringBuilder sb = new StringBuilder();

        sb.append("DECLARE @sessionUser	        VARCHAR(MAX) = ? ");
        sb.append("	      ,@userOnScreen		VARCHAR(MAX) = ? ");
        sb.append("	      ,@PageNumber		    INT = ? ");
        sb.append("       ,@RowsOfPage		    INT = ? ");
        sb.append("  ");
        sb.append("SELECT u.first_name  ");
        sb.append("      ,u.username  ");
        sb.append("	     ,u.biography  ");
        sb.append("	     ,u.private_account ");
        sb.append("	     ,CONVERT(BIT, 0) isFollowedBySessionUser ");
        sb.append("      ,CONVERT(BIT, IIF(f2.followed_identifier IS NOT NULL, 1, 0)) isFollowingSessionUser  ");
        sb.append("	     ,u.profile_photo_url profile_photo_url ");
        sb.append("	     ,u.verified ");
        sb.append("	     ,u.identifier ");
        sb.append("FROM Users u   ");
        sb.append("LEFT JOIN users_follows f   ");
        sb.append("  ON f.follower_identifier = @sessionUser  ");
        sb.append("  AND f.followed_identifier = u.identifier   ");
        sb.append("LEFT JOIN users_follows f2   ");
        sb.append("	ON f2.followed_identifier = @sessionUser   ");
        sb.append("	AND f2.follower_identifier = u.identifier ");
        sb.append("WHERE f.follower_identifier is null ");
        sb.append("	AND u.identifier <> @sessionUser ");
        sb.append("	AND (@userOnScreen IS NULL OR u.identifier <> @userOnScreen) ");
        sb.append("ORDER BY NEWID() ");
        sb.append("OFFSET (@PageNumber) * @RowsOfPage ROWS  ");
        sb.append("FETCH NEXT @RowsOfPage ROWS ONLY   ");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, sessionUserIdentifier);
        query.setParameter(2, userOnScreen);
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
                    .isVerified((Boolean) result[7])
                    .userIdentifier((String) result[8])
                    .build());
        }

        return response;
    }
}
