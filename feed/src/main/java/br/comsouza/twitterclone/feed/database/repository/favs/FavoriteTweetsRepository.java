package br.comsouza.twitterclone.feed.database.repository.favs;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.aws.IAmazonService;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
import br.comsouza.twitterclone.feed.util.TweetResponseUtil;
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
    private final IAmazonService iAmazonService;

    public FavoriteTweetsRepository(EntityManager em,
                                    IAmazonService iAmazonService) {
        this.em = em;
        this.iAmazonService = iAmazonService;
    }

    public List<TimelineTweetResponse> find(String sessionUserIdentifier, Integer page, Integer size) throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @sessionUserId VARCHAR(MAX) = ? ");
        sb.append("	   ,@PageNumber				INT = ? ");
        sb.append("       ,@RowsOfPage				INT = ? ");
        sb.append(" ");
        sb.append(TweetResponseUtil.COMMON_QUERY);
        sb.append("	     ,f.time ");
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

        List<Object[]> lista = query.getResultList();

        List<TimelineTweetResponse> response = new ArrayList<>();
        for(Object[] result : lista){
            response.add(new TimelineTweetResponse(result, iAmazonService));
        }
        return response;
    }
}
