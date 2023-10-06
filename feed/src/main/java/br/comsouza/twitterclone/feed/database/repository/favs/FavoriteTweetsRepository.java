package br.comsouza.twitterclone.feed.database.repository.favs;

import br.comsouza.twitterclone.feed.client.IAccountsClient;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.handler.exceptions.ServerSideErrorException;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FavoriteTweetsRepository {

    @PersistenceContext
    private final EntityManager em;
    private final IInteractionsService iInteractionsService;

    public FavoriteTweetsRepository(EntityManager em,
                                    IInteractionsService iInteractionsService) {
        this.em = em;
        this.iInteractionsService = iInteractionsService;
    }

    public List<TimelineTweetResponse> find(String sessionUserIdentifier, Integer page, Integer size) throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @sessionUserId VARCHAR(MAX) = ? ");
        sb.append("	   ,@PageNumber				INT = ? ");
        sb.append("       ,@RowsOfPage				INT = ? ");
        sb.append(" ");
        sb.append("SELECT t.tweet_identifier  ");
        sb.append("	  ,t.original_tweet_identifier  ");
        sb.append("	  ,p.description  ");
        sb.append("	  ,u.identifier  ");
        sb.append("	  ,u.username  ");
        sb.append("	  ,u.first_name  ");
        sb.append("	  ,u.profile_photo_identifier  ");
        sb.append("	  ,t.message  ");
        sb.append("	  ,t.attachment  ");
        sb.append("	  ,f.time ");
        sb.append("FROM tweets t ");
        sb.append("INNER JOIN tweets_favs f ");
        sb.append("	ON f.user_identifier = @sessionUserId ");
        sb.append("	AND f.tweet_identifier = t.tweet_identifier ");
        sb.append("INNER JOIN users u ");
        sb.append("	ON u.identifier =  t.user_identifier ");
        sb.append("INNER JOIN tweets_types p ");
        sb.append("	ON p.type_identifier = t.type ");
        sb.append("ORDER BY f.time desc ");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, sessionUserIdentifier);
        query.setParameter(2, page);
        query.setParameter(3, size);

        List<Object[]> list = query.getResultList();

        List<TimelineTweetResponse> response = new ArrayList<>();
        if (!list.isEmpty()) {
            for (Object[] result : list) {
                response.add(TimelineTweetResponse.builder()
                        .tweetIdentifier((String) result[0])
                        .originalTweetIdentifier((String) result[1])
                        .tweetTypeDescription((String) result[2])
                        .userIdentifier((String) result[3])
                        .userUsername((String) result[4])
                        .userFirstName((String) result[5])
                        .userProfilePhotoUrl((String) result[6])
                        .tweetMessage((String) result[7])
                        .tweetAttachment((byte[]) result[8])
                        .tweetCommentsCount(iInteractionsService.getAllTweetCommentsCount((String) result[0]))
                        .tweetRetweetsCount(iInteractionsService.getTweetAllRetweetsTypesCount((String) result[0]))
                        .tweetLikesCount(iInteractionsService.getTweetLikesCount((String) result[0]))
                        .tweetViewsCount(iInteractionsService.getTweetViewsCount((String) result[0]))
                        .isLikedByMe(iInteractionsService.verifyIsLiked((String) result[0], sessionUserIdentifier).isPresent())
                        .isRetweetedByMe(iInteractionsService.verifyIsRetweeted((String) result[0], sessionUserIdentifier).isPresent())
                        .originalTweetResponse(null)
                        .build());
            }
        }
        return response;
    }
}
