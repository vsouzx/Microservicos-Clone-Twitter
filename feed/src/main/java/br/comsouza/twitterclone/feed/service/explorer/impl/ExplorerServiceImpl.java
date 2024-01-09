package br.comsouza.twitterclone.feed.service.explorer.impl;

import br.comsouza.twitterclone.feed.database.repository.explore.factory.ExplorerStrategyFactory;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.explorer.IExplorerService;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExplorerServiceImpl implements IExplorerService{

    private final ExplorerStrategyFactory explorerStrategyFactory;
    private final IPostsService iPostsService;

    public ExplorerServiceImpl(ExplorerStrategyFactory explorerStrategyFactory,
                               IPostsService iPostsService) {
        this.explorerStrategyFactory = explorerStrategyFactory;
        this.iPostsService = iPostsService;
    }

    @Override
    @Cacheable(value = "explore-tweets")
    public List<TimelineTweetResponse> find(String type, String keyword, Integer page, Integer size, String sessionUserIdentifier) throws Exception {
        List<TimelineTweetResponse> posts = explorerStrategyFactory.getStrategy(type).find(keyword, page, size, sessionUserIdentifier);

        for(TimelineTweetResponse post : posts){
            iPostsService.loadTweetResponses(post, sessionUserIdentifier);
        }

        return posts;
    }
}
