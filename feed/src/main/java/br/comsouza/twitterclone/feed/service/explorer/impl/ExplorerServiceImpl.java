package br.comsouza.twitterclone.feed.service.explorer.impl;

import br.comsouza.twitterclone.feed.database.repository.explore.IExploreStrategy;
import br.comsouza.twitterclone.feed.database.repository.explore.factory.ExplorerStrategyFactory;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.explorer.IExplorerService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExplorerServiceImpl implements IExplorerService{

    private final ExplorerStrategyFactory explorerStrategyFactory;

    public ExplorerServiceImpl(ExplorerStrategyFactory explorerStrategyFactory) {
        this.explorerStrategyFactory = explorerStrategyFactory;
    }

    @Override
    public List<TimelineTweetResponse> find(String type, String keyword, Integer page, Integer size, String sessionUserIdentifier) throws Exception {
        IExploreStrategy strategy = explorerStrategyFactory.getStrategy(type);
        return strategy.find(keyword, page, size, sessionUserIdentifier);
    }
}
