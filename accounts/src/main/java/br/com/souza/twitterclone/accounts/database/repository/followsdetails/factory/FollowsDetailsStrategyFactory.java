package br.com.souza.twitterclone.accounts.database.repository.followsdetails.factory;

import br.com.souza.twitterclone.accounts.database.repository.followsdetails.IFollowsDetailsStrategy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class FollowsDetailsStrategyFactory {

    private final Map<String, IFollowsDetailsStrategy> strategies = new HashMap<>();

    public FollowsDetailsStrategyFactory(Set<IFollowsDetailsStrategy> strategySet){
        strategySet.forEach(strategy -> strategies.put(strategy.getStrategyName(), strategy));
    }

    public IFollowsDetailsStrategy getStrategy(String type) throws Exception {
        return strategies.get(type.toUpperCase());
    }
}
