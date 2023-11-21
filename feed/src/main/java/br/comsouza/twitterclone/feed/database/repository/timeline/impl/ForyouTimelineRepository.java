package br.comsouza.twitterclone.feed.database.repository.timeline.impl;

import br.comsouza.twitterclone.feed.database.repository.timeline.ITimelineStrategy;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.aws.IAmazonService;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
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
    private final IInteractionsService iInteractionsService;
    private final IAmazonService iAmazonService;

    public ForyouTimelineRepository(EntityManager em,
                                    IInteractionsService iInteractionsService,
                                    IAmazonService iAmazonService) {
        this.em = em;
        this.iInteractionsService = iInteractionsService;
        this.iAmazonService = iAmazonService;
    }

    @Override
    public List<TimelineTweetResponse> getTimeLine(String sessionUserIdentifier, Integer page, Integer size, String targetUserIdentifier) throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @sessionUserIdentifier VARCHAR(MAX)  = ? ");
        sb.append("	      ,@PageNumber			  INT           = ? ");
        sb.append("       ,@RowsOfPage			  INT           = ? ");
        sb.append("  ");
        sb.append("SELECT t.tweet_identifier  ");
        sb.append("	  ,t.original_tweet_identifier  ");
        sb.append("	  ,p.description  ");
        sb.append("	  ,u.identifier  ");
        sb.append("	  ,u.username  ");
        sb.append("   ,u.first_name  ");
        sb.append("	  ,u.profile_photo_url  ");
        sb.append("	  ,t.message  ");
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

        return lista.stream()
                .map(result -> {
                    try {
                        return new TimelineTweetResponse(result, iAmazonService, iInteractionsService, sessionUserIdentifier);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    @Override
    public String getStrategyName() {
        return "FORYOU";
    }
}
