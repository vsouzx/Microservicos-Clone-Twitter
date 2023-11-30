package br.comsouza.twitterclone.feed.database.repository.postdetails;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.aws.IAmazonService;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class PostResumeRepository {

    @PersistenceContext
    private final EntityManager em;
    private final IInteractionsService iInteractionsService;
    private final IAmazonService iAmazonService;

    public PostResumeRepository(EntityManager em,
                                IInteractionsService iInteractionsService,
                                IAmazonService iAmazonService) {
        this.em = em;
        this.iInteractionsService = iInteractionsService;
        this.iAmazonService = iAmazonService;
    }

    public TimelineTweetResponse find(String sessionUserIdentifier, String targetTweetIdentifier) throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @targetTweetIdentifier VARCHAR(MAX) = ?  ");
        sb.append("       ,@sessionUserIdentifier VARCHAR(MAX) = ?  ");
        sb.append("  ");
        sb.append("SELECT t.tweet_identifier  ");
        sb.append("	  ,t.original_tweet_identifier  ");
        sb.append("	  ,p.description  ");
        sb.append("	  ,u.identifier  ");
        sb.append("	  ,u.username  ");
        sb.append("	  ,u.first_name  ");
        sb.append("	  ,u.profile_photo_url  ");
        sb.append("	  ,t.message  ");
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
        sb.append("			AND tp.description IN ('RETWEET', 'NO_VALUE_RETWEET')) retweetCount ");
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
        sb.append("		 AND tp.description IN ('RETWEET', 'NO_VALUE_RETWEET')) isRetweetedByMe ");
        sb.append("		 ,t.has_attachment ");
        sb.append("		 ,u.verified ");
        sb.append("		 ,t.publication_time ");
        sb.append("FROM tweets t  ");
        sb.append("INNER JOIN users u  ");
        sb.append("	ON u.identifier = t.user_identifier  ");
        sb.append("INNER JOIN tweets_types p  ");
        sb.append("	ON p.type_identifier = t.type  ");
        sb.append("WHERE t.tweet_identifier = @targetTweetIdentifier ");
        sb.append("ORDER BY t.publication_time desc  ");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, targetTweetIdentifier);
        query.setParameter(2, sessionUserIdentifier);

        Object[] result = (Object[]) query.getSingleResult();

        return new TimelineTweetResponse(result, iAmazonService);
    }
}
