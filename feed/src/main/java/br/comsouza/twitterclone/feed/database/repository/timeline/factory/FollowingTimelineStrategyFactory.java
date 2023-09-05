package br.comsouza.twitterclone.feed.database.repository.timeline.factory;

import br.comsouza.twitterclone.feed.database.repository.timeline.ITimelineStrategy;
import br.comsouza.twitterclone.feed.database.repository.timeline.impl.FollowingTimelineRepository;
import br.comsouza.twitterclone.feed.database.repository.timeline.impl.ForyouTimelineRepository;
import org.springframework.stereotype.Service;

@Service
public class FollowingTimelineStrategyFactory {

    private final FollowingTimelineRepository followingTimelineRepository;
    private final ForyouTimelineRepository foryouTimelineRepository;

    public FollowingTimelineStrategyFactory(FollowingTimelineRepository followingTimelineRepository,
                                            ForyouTimelineRepository foryouTimelineRepository) {
        this.followingTimelineRepository = followingTimelineRepository;
        this.foryouTimelineRepository = foryouTimelineRepository;
    }

    public ITimelineStrategy getStrategy(String type) throws Exception {
        return switch (type) {
            case "foryou" -> foryouTimelineRepository;
            case "following" -> followingTimelineRepository;
            default -> throw new Exception("Type not supported: " + type);
        };
    }
}
