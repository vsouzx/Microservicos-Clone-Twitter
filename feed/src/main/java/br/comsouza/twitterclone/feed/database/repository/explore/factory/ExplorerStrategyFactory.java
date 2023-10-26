package br.comsouza.twitterclone.feed.database.repository.explore.factory;

import br.comsouza.twitterclone.feed.database.repository.explore.IExploreStrategy;
import br.comsouza.twitterclone.feed.database.repository.explore.impl.ExploreByLatterRepository;
import br.comsouza.twitterclone.feed.database.repository.explore.impl.ExploreByMainRepository;
import br.comsouza.twitterclone.feed.database.repository.explore.impl.ExploreByMediaRepository;
import org.springframework.stereotype.Service;

@Service
public class ExplorerStrategyFactory {

    private final ExploreByLatterRepository exploreByLatterRepository;
    private final ExploreByMainRepository exploreByMainRepository;
    private final ExploreByMediaRepository exploreByMediaRepository;

    public ExplorerStrategyFactory(ExploreByLatterRepository exploreByLatterRepository,
                                   ExploreByMainRepository exploreByMainRepository,
                                   ExploreByMediaRepository exploreByMediaRepository) {
        this.exploreByLatterRepository = exploreByLatterRepository;
        this.exploreByMainRepository = exploreByMainRepository;
        this.exploreByMediaRepository = exploreByMediaRepository;
    }

    public IExploreStrategy getStrategy(String type) throws Exception {
        return switch (type.toUpperCase()) {
            case "LATTER" -> exploreByLatterRepository;
            case "MAIN" -> exploreByMainRepository;
            case "MEDIA" -> exploreByMediaRepository;
            default -> throw new Exception("Type not supported: " + type);
        };
    }
}
