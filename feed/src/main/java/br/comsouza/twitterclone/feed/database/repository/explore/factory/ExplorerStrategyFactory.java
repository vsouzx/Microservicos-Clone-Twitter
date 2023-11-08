package br.comsouza.twitterclone.feed.database.repository.explore.factory;

import br.comsouza.twitterclone.feed.database.repository.explore.IExploreStrategy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class ExplorerStrategyFactory {

    private final Map<String, IExploreStrategy> strategies = new HashMap<>();

    public ExplorerStrategyFactory(Set<IExploreStrategy> strategySet){
        strategySet.forEach(strategy -> strategies.put(strategy.getStrategyName(), strategy));
    }

    public IExploreStrategy getStrategy(String type) throws Exception {
        IExploreStrategy strategy = strategies.get(type.toUpperCase());
        if(strategy == null){
            throw new Exception("Invalid type");
        }
        return strategy;
    }
}
