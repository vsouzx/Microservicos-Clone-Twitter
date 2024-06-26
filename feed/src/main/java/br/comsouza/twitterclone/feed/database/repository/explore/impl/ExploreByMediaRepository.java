package br.comsouza.twitterclone.feed.database.repository.explore.impl;

import br.comsouza.twitterclone.feed.database.repository.explore.IExploreStrategy;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.aws.IAmazonService;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
import br.comsouza.twitterclone.feed.util.TweetResponseUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExploreByMediaRepository implements IExploreStrategy {

    @PersistenceContext
    private final EntityManager entityManager;
    private final IAmazonService iAmazonService;

    public ExploreByMediaRepository(EntityManager entityManager,
                                    IAmazonService iAmazonService) {
        this.entityManager = entityManager;
        this.iAmazonService = iAmazonService;
    }

    @Override
    public List<TimelineTweetResponse> find(String keyword, Integer page, Integer size, String sessionUserIdentifier) throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @keyWord                 VARCHAR(MAX)   = ? ");
        sb.append("	      ,@PageNumber				INT            = ? ");
        sb.append("       ,@RowsOfPage				INT            = ? ");
        sb.append("	      ,@sessionUserIdentifier   VARCHAR(MAX)   = ? ");
        sb.append("   ");
        sb.append(TweetResponseUtil.COMMON_QUERY);
        sb.append("FROM tweets t    ");
        sb.append("INNER JOIN users u    ");
        sb.append("	ON u.identifier = t.user_identifier   ");
        sb.append("INNER JOIN tweets_types p    ");
        sb.append("	ON p.type_identifier = t.type    ");
        sb.append("LEFT JOIN silenced_users s    ");
        sb.append("	ON s.silenced_identifier = t.user_identifier    ");
        sb.append("	AND s.silencer_identifier = @sessionUserIdentifier    ");
        sb.append("LEFT JOIN blocked_users b    ");
        sb.append("	ON b.blocked_identifier = t.user_identifier    ");
        sb.append("	AND b.blocker_identifier = @sessionUserIdentifier   ");
        sb.append("WHERE s.silenced_identifier IS NULL   ");
        sb.append("	AND b.blocked_identifier IS NULL   ");
        sb.append("	AND t.message IS NOT NULL  ");
        sb.append("	AND (@keyWord IS NULL OR UPPER(t.message) LIKE '%' + UPPER(@keyWord) + '%')   ");
        sb.append("	AND t.has_attachment = CONVERT(BIT, 1)  ");
        sb.append("ORDER BY NEWID()  ");
        sb.append("OFFSET (@PageNumber) * @RowsOfPage ROWS      ");
        sb.append("FETCH NEXT @RowsOfPage ROWS ONLY      ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter(1, keyword);
        query.setParameter(2, page);
        query.setParameter(3, size);
        query.setParameter(4, sessionUserIdentifier);

        List<Object[]> lista = query.getResultList();

        List<TimelineTweetResponse> response = new ArrayList<>();
        for(Object[] result : lista){
            response.add(new TimelineTweetResponse(result, iAmazonService));
        }
        return response;
    }

    @Override
    public String getStrategyName() {
        return "MEDIA";
    }
}
