package br.comsouza.twitterclone.feed.database.repository.timeline;

import br.comsouza.twitterclone.feed.dto.handler.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FollowingTimelineRepository {

    @PersistenceContext
    private final EntityManager em;

    private final IInteractionsService iInteractionsService;


    public FollowingTimelineRepository(EntityManager em,
                                       IInteractionsService iInteractionsService) {
        this.em = em;
        this.iInteractionsService = iInteractionsService;
    }

    public List<TimelineTweetResponse> find(String sessionUserIdentifier) {

        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @sessionUserIdentifier VARCHAR(MAX) = ? ");
        sb.append(" ");
        sb.append("SELECT t.tweet_identifier ");
        sb.append("	  ,t.original_tweet_identifier ");
        sb.append("	  ,p.description ");
        sb.append("	  ,u.identifier ");
        sb.append("	  ,u.username ");
        sb.append("   ,u.first_name ");
        sb.append("	  ,u.profile_photo ");
        sb.append("	  ,t.message ");
        sb.append("	  ,t.attachment ");
        sb.append("FROM tweets t ");
        sb.append("INNER JOIN users_follows f ");
        sb.append("	ON f.follower_identifier = @sessionUserIdentifier ");
        sb.append("	AND f.followed_identifier = t.user_identifier ");
        sb.append("INNER JOIN users u ");
        sb.append("	ON u.identifier = f.followed_identifier ");
        sb.append("INNER JOIN tweets_types p ");
        sb.append("	ON p.type_identifier = t.type ");
        sb.append("LEFT JOIN silenced_users s ");
        sb.append("	ON s.silenced_identifier = f.followed_identifier ");
        sb.append("	AND s.silencer_identifier = f.follower_identifier ");
        sb.append("WHERE s.silenced_identifier IS NULL ");
        sb.append("ORDER BY t.publication_time desc ");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, sessionUserIdentifier);

        List<Object[]> list = query.getResultList();

        List<TimelineTweetResponse> response = new ArrayList<>();

        if (!list.isEmpty()) {
            list.stream().forEach(result -> {
                response.add(TimelineTweetResponse.builder()
                        .tweetIdentifier((String) result[0])
                        .originalTweetIdentifier((String) result[1])
                        .tweetTypeDescription((String) result[2])
                        .userIdentifier((String) result[3])
                        .userUsername((String) result[4])
                        .userFirstName((String) result[5])
                        .userProfilePhoto((byte[]) result[6])
                        .tweetMessage((String) result[7])
                        .tweetAttachment((byte[]) result[8])
                        .tweetCommentsCount(iInteractionsService.getTweetCommentsCount((String) result[0]))
                        .tweetRetweetsCount(iInteractionsService.getTweetRetweetsCount((String) result[0]))
                        .tweetLikesCount(iInteractionsService.getTweetLikesCount((String) result[0]))
                        .tweetViewsCount(iInteractionsService.getTweetViewsCount((String) result[0]))
                        .originalTweetResponse(null) //TODO: adicionar lógica para pegar response de um tweet por id
                        .build());
            });
        }
        return response;
    }
}
