package br.comsouza.twitterclone.feed.database.repository.postdetails;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.aws.IAmazonService;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
import br.comsouza.twitterclone.feed.util.TweetResponseUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

@Repository
public class PostDetailsRepository {

    @PersistenceContext
    private final EntityManager em;
    private final IAmazonService iAmazonService;

    public PostDetailsRepository(EntityManager em,
                                 IAmazonService iAmazonService) {
        this.em = em;
        this.iAmazonService = iAmazonService;
    }

    public TimelineTweetResponse find(String sessionUserIdentifier, String targetTweetIdentifier) throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @targetTweetIdentifier VARCHAR(MAX) = ?  ");
        sb.append("       ,@sessionUserIdentifier VARCHAR(MAX) = ?  ");
        sb.append("  ");
        sb.append(TweetResponseUtil.COMMON_QUERY);
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
