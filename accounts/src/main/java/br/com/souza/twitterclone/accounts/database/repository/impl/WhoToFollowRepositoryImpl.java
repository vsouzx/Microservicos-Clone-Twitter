package br.com.souza.twitterclone.accounts.database.repository.impl;

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
public class WhoToFollowRepositoryImpl {

    @PersistenceContext
    private final EntityManager em;
    private final IUsersInteractionsService iUsersInteractionsService;

    public WhoToFollowRepositoryImpl(EntityManager em,
                                     IUsersInteractionsService iUsersInteractionsService) {
        this.em = em;
        this.iUsersInteractionsService = iUsersInteractionsService;
    }

    public List<UserDetailsByIdentifierResponse> find(String sessionUserIdentifier, Integer page, Integer size, String userOnScreen, String authorization) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("DECLARE @sessionUser	        VARCHAR(MAX) = ? ");
        sb.append("	      ,@userOnScreen		VARCHAR(MAX) = ? ");
        sb.append("	      ,@PageNumber		    INT = ? ");
        sb.append("       ,@RowsOfPage		    INT = ? ");
        sb.append("  ");
        sb.append("SELECT u.identifier  ");
        sb.append("      ,u.first_name  ");
        sb.append("	     ,u.username  ");
        sb.append("	     ,u.biography ");
        sb.append("	     ,u.location  ");
        sb.append("	     ,u.site  ");
        sb.append("	     ,u.registrationTime  ");
        sb.append("	     ,u.privateAccount  ");
        sb.append("	     ,u.profile_photo_url  ");
        sb.append("	     ,u.background_photo_url ");
        sb.append("	     ,u.verified ");
        sb.append("FROM Users u   ");
        sb.append("LEFT JOIN blocked_users b   ");
        sb.append("	ON b.blocked_identifier = @sessionUser   ");
        sb.append("	AND b.blocker_identifier = u.identifier ");
        sb.append("LEFT JOIN blocked_users b2   ");
        sb.append("	ON b2.blocker_identifier = @sessionUser   ");
        sb.append("	AND b2.blocked_identifier = u.identifier ");
        sb.append("WHERE b.blocker_identifier is null ");
        sb.append("	AND b2.blocker_identifier is null ");
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
                    .backgroundPhotoUrl((String) result[9])
                    .isVerified((Boolean) result[10])
                    .following(iUsersInteractionsService.getUserFollowsCount((String) result[0]))
                    .followers(iUsersInteractionsService.getUserFollowersCount((String) result[0]))
                    .isBlockedByMe(iUsersInteractionsService.verifyIfIsBlocked(sessionUserIdentifier, (String) result[0]).isPresent())
                    .hasBlockedMe(iUsersInteractionsService.verifyIfIsBlocked((String) result[0], sessionUserIdentifier).isPresent())
                    .isFollowedByMe(iUsersInteractionsService.verifyIfIsFollowing(sessionUserIdentifier, (String) result[0]).isPresent())
                    .isPendingFollowedByMe(iUsersInteractionsService.verifyIfIsPendingFollowing(sessionUserIdentifier, (String) result[0]).isPresent())
                    .isFollowingMe(iUsersInteractionsService.verifyIfIsFollowing((String) result[0], sessionUserIdentifier).isPresent())
                    .isSilencedByMe(iUsersInteractionsService.verifyIfIsSilenced(sessionUserIdentifier, (String) result[0]).isPresent())
                    .isNotificationsAlertedByMe(iUsersInteractionsService.verifyIfIsAlerted(sessionUserIdentifier, (String) result[0]).isPresent())
                    .tweetsCount(iUsersInteractionsService.getTweetsCount((String) result[0], authorization))
                    .followersInCommon(iUsersInteractionsService.getCommonFollowers(sessionUserIdentifier, (String) result[0]))
                    .build());
        }

        return response;
    }
}
