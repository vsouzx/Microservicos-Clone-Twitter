package br.comsouza.twitterclone.feed.database.repository.timeline.factory;

import br.comsouza.twitterclone.feed.database.repository.timeline.ITimelineStrategy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class FollowingTimelineStrategyFactory {

    private final Map<String, ITimelineStrategy> strategies = new HashMap<>();

    public FollowingTimelineStrategyFactory(Set<ITimelineStrategy> strategySet){
        strategySet.forEach(strategy -> strategies.put(strategy.getStrategyName(), strategy));
    }

    public ITimelineStrategy getStrategy(String type) throws Exception{
        ITimelineStrategy strategy = strategies.get(type.toUpperCase());
        if(strategy == null){
            throw new Exception("Invalid type");
        }
        return strategy;
    }
}
