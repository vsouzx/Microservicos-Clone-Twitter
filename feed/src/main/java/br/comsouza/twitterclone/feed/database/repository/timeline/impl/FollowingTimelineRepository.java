package br.comsouza.twitterclone.feed.database.repository.timeline.impl;

import br.comsouza.twitterclone.feed.database.repository.timeline.ITimelineStrategy;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.aws.IAmazonService;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FollowingTimelineRepository implements ITimelineStrategy {

    @PersistenceContext
    private final EntityManager em;
    private final IAmazonService iAmazonService;

    public FollowingTimelineRepository(EntityManager em,
                                       IAmazonService iAmazonService) {
        this.em = em;
        this.iAmazonService = iAmazonService;
    }

    @Override
    public List<TimelineTweetResponse> getTimeLine(String sessionUserIdentifier, Integer page, Integer size, String targetUserIdentifier) throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @sessionUserIdentifier VARCHAR(MAX) = ? ");
        sb.append("	      ,@PageNumber				INT = ?  ");
        sb.append("       ,@RowsOfPage				INT = ?     ");
        sb.append(" ");
        sb.append("SELECT t.tweet_identifier ");
        sb.append("	  ,t.original_tweet_identifier ");
        sb.append("	  ,p.description ");
        sb.append("	  ,u.identifier ");
        sb.append("	  ,u.username ");
        sb.append("   ,u.first_name ");
        sb.append("	  ,u.profile_photo_url ");
        sb.append("	  ,t.message ");
        sb.append("	  ,(SELECT COUNT(*)  ");
        sb.append("	    FROM tweets t2 ");
        sb.append("		INNER JOIN tweets_types tp ");
        sb.append("			ON tp.type_identifier = t2.type ");
        sb.append("		WHERE t2.original_tweet_identifier = t.tweet_identifier  ");
        sb.append("			AND tp.description = 'COMMENT') commentsCount ");
        sb.append("	   ,(SELECT COUNT(*)  ");
        sb.append("	    FROM tweets t2 ");
        sb.append("		INNER JOIN tweets_types tp ");
        sb.append("			ON tp.type_identifier = t2.type ");
        sb.append("		WHERE t2.original_tweet_identifier = t.tweet_identifier  ");
        sb.append("			AND tp.description = 'RETWEET') retweetCount ");
        sb.append("	   ,(SELECT COUNT(*)  ");
        sb.append("	     FROM tweets_likes  ");
        sb.append("		 WHERE tweet_identifier = t.tweet_identifier) likesCount ");
        sb.append("	   ,(SELECT COUNT(*)  ");
        sb.append("	     FROM tweets_views  ");
        sb.append("		 WHERE tweet_identifier = t.tweet_identifier) viewsCount ");
        sb.append("	   ,(SELECT COUNT(*)  ");
        sb.append("	     FROM tweets_favs   ");
        sb.append("		 WHERE tweet_identifier = t.tweet_identifier) favsCount ");
        sb.append("	   ,(SELECT IIF(MAX(tweet_identifier) IS NULL, CONVERT(BIT, 0), CONVERT(BIT, 1)) ");
        sb.append("	     FROM tweets_likes  ");
        sb.append("		 WHERE tweet_identifier = t.tweet_identifier ");
        sb.append("		 AND user_identifier = @sessionUserIdentifier) isLikedByMe ");
        sb.append("	   ,(SELECT IIF(MAX(tweet_identifier) IS NULL, CONVERT(BIT, 0), CONVERT(BIT, 1)) ");
        sb.append("	     FROM tweets_favs  ");
        sb.append("		 WHERE tweet_identifier = t.tweet_identifier ");
        sb.append("		 AND user_identifier = @sessionUserIdentifier) isFavoritedByMe ");
        sb.append("	   ,(SELECT IIF(MAX(t2.tweet_identifier) IS NULL, CONVERT(BIT, 0), CONVERT(BIT, 1)) ");
        sb.append("	     FROM tweets t2 ");
        sb.append("		 INNER JOIN tweets_types tp ");
        sb.append("			ON tp.type_identifier = t2.type ");
        sb.append("		 WHERE t2.user_identifier = @sessionUserIdentifier ");
        sb.append("		 AND t2.original_tweet_identifier = t.tweet_identifier ");
        sb.append("		 AND tp.description = 'RETWEET') isRetweetedByMe ");
        sb.append("		 ,t.has_attachment ");
        sb.append("		 ,u.verified ");
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
        sb.append("OFFSET (@PageNumber) * @RowsOfPage ROWS   ");
        sb.append("FETCH NEXT @RowsOfPage ROWS ONLY   ");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, sessionUserIdentifier);
        query.setParameter(2, page);
        query.setParameter(3, size);

        List<Object[]> lista = query.getResultList();

        List<TimelineTweetResponse> response = new ArrayList<>();
        for(Object[] result : lista){
            response.add(new TimelineTweetResponse(result, iAmazonService));
        }
        return response;
    }

    @Override
    public String getStrategyName() {
        return "FOLLOWING";
    }

}
