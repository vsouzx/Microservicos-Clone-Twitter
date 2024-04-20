package br.comsouza.twitterclone.feed.database.repository.timeline.impl;

import br.comsouza.twitterclone.feed.database.repository.timeline.ITimelineStrategy;
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
public class ForyouTimelineRepository implements ITimelineStrategy {

    @PersistenceContext
    private final EntityManager em;
    private final IAmazonService iAmazonService;

    public ForyouTimelineRepository(EntityManager em,
                                    IAmazonService iAmazonService) {
        this.em = em;
        this.iAmazonService = iAmazonService;
    }

    @Override
    public List<TimelineTweetResponse> getTimeLine(String sessionUserIdentifier, Integer page, Integer size, String targetUserIdentifier) throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @sessionUserIdentifier VARCHAR(MAX)  = ? ");
        sb.append("	      ,@PageNumber			  INT           = ? ");
        sb.append("       ,@RowsOfPage			  INT           = ? ");
        sb.append("  ");
        sb.append(TweetResponseUtil.COMMON_QUERY);
        sb.append("FROM tweets t  ");
        sb.append("INNER JOIN users u  ");
        sb.append("	ON u.identifier = t.user_identifier ");
        sb.append("INNER JOIN tweets_types p  ");
        sb.append("	ON p.type_identifier = t.type  ");
        sb.append("LEFT JOIN silenced_users s  ");
        sb.append("	ON s.silenced_identifier = t.user_identifier  ");
        sb.append("	AND s.silencer_identifier = @sessionUserIdentifier  ");
        sb.append("LEFT JOIN blocked_users b  ");
        sb.append("	ON b.blocked_identifier = t.user_identifier  ");
        sb.append("	AND b.blocker_identifier = @sessionUserIdentifier  ");
        sb.append("WHERE s.silenced_identifier IS NULL AND b.blocked_identifier IS NULL ");
        sb.append("ORDER BY t.publication_time desc  ");
        sb.append("OFFSET (@PageNumber) * @RowsOfPage ROWS    ");
        sb.append("FETCH NEXT @RowsOfPage ROWS ONLY    ");

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
        return "FORYOU";
    }
}
