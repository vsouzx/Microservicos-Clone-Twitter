package br.com.souza.twitterclone.accounts.database.repository.followsdetails.impl;

import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.database.repository.followsdetails.IFollowsDetailsStrategy;
import br.com.souza.twitterclone.accounts.service.interactions.IUsersInteractionsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class UserKnownFollowersRepository implements IFollowsDetailsStrategy {

    @PersistenceContext
    private final EntityManager em;
    private final IUsersInteractionsService usersInteractionsService;

    public UserKnownFollowersRepository(EntityManager em,
                                        IUsersInteractionsService usersInteractionsService) {
        this.em = em;
        this.usersInteractionsService = usersInteractionsService;
    }

    @Override
    public List<UserDetailsByIdentifierResponse> getUserFollowsInformations(String sessionUserIdentifier, String targetUserIdentifier, Integer page, Integer size, String authorization){
        StringBuilder sb = new StringBuilder();

        sb.append("DECLARE @sessionUser	    VARCHAR(MAX) = ?  ");
        sb.append("	   ,@targetUser			VARCHAR(MAX) = ?  ");
        sb.append("	   ,@PageNumber			INT = ? ");
        sb.append("    ,@RowsOfPage			INT = ?    ");
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
        sb.append("FROM users u     ");
        sb.append("INNER JOIN users_follows f     ");
        sb.append("	ON f.followed_identifier = @targetUser    ");
        sb.append("	AND f.follower_identifier = u.identifier  ");
        sb.append("LEFT JOIN users_follows common     ");
        sb.append("	ON common.followed_identifier = f.follower_identifier  ");
        sb.append("	AND common.follower_identifier = @sessionUser  ");
        sb.append("WHERE common.follower_identifier IS NOT NULL ");
        sb.append("ORDER BY u.identifier   ");
        sb.append("OFFSET (@PageNumber) * @RowsOfPage ROWS   ");
        sb.append("FETCH NEXT @RowsOfPage ROWS ONLY   ");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, sessionUserIdentifier);
        query.setParameter(2, targetUserIdentifier);
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
                    .following(usersInteractionsService.getUserFollowsCount((String) result[0]))
                    .followers(usersInteractionsService.getUserFollowersCount((String) result[0]))
                    .isBlockedByMe(usersInteractionsService.verifyIfIsBlocked(sessionUserIdentifier, (String) result[0]).isPresent())
                    .hasBlockedMe(usersInteractionsService.verifyIfIsBlocked((String) result[0], sessionUserIdentifier).isPresent())
                    .isFollowedByMe(usersInteractionsService.verifyIfIsFollowing(sessionUserIdentifier, (String) result[0]).isPresent())
                    .isPendingFollowedByMe(usersInteractionsService.verifyIfIsPendingFollowing(sessionUserIdentifier, (String) result[0]).isPresent())
                    .isFollowingMe(usersInteractionsService.verifyIfIsFollowing((String) result[0], sessionUserIdentifier).isPresent())
                    .isSilencedByMe(usersInteractionsService.verifyIfIsSilenced(sessionUserIdentifier, (String) result[0]).isPresent())
                    .isNotificationsAlertedByMe(usersInteractionsService.verifyIfIsAlerted(sessionUserIdentifier, (String) result[0]).isPresent())
                    .tweetsCount(usersInteractionsService.getTweetsCount((String) result[0], authorization))
                    .build());
        }

        return response;
    }
}
