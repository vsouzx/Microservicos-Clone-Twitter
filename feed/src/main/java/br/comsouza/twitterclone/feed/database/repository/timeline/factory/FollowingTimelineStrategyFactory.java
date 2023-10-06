package br.comsouza.twitterclone.feed.database.repository.timeline.factory;

import br.comsouza.twitterclone.feed.database.repository.timeline.ITimelineStrategy;
import br.comsouza.twitterclone.feed.database.repository.timeline.impl.FollowingTimelineRepository;
import br.comsouza.twitterclone.feed.database.repository.timeline.impl.ForyouTimelineRepository;
import br.comsouza.twitterclone.feed.database.repository.timeline.impl.LikesTimelineRepository;
import br.comsouza.twitterclone.feed.database.repository.timeline.impl.MediasTimelineRepository;
import br.comsouza.twitterclone.feed.database.repository.timeline.impl.PostsTimelineRepository;
import br.comsouza.twitterclone.feed.database.repository.timeline.impl.RepliesTimelineRepository;
import org.springframework.stereotype.Service;

@Service
public class FollowingTimelineStrategyFactory {

    private final FollowingTimelineRepository followingTimelineRepository;
    private final ForyouTimelineRepository foryouTimelineRepository;
    private final PostsTimelineRepository postsTimelineRepository;
    private final RepliesTimelineRepository repliesTimelineRepository;
    private final MediasTimelineRepository mediasTimelineRepository;
    private final LikesTimelineRepository likesTimelineRepository;

    public FollowingTimelineStrategyFactory(FollowingTimelineRepository followingTimelineRepository,
                                            ForyouTimelineRepository foryouTimelineRepository,
                                            PostsTimelineRepository postsTimelineRepository,
                                            RepliesTimelineRepository repliesTimelineRepository,
                                            MediasTimelineRepository mediasTimelineRepository,
                                            LikesTimelineRepository likesTimelineRepository) {
        this.followingTimelineRepository = followingTimelineRepository;
        this.foryouTimelineRepository = foryouTimelineRepository;
        this.postsTimelineRepository = postsTimelineRepository;
        this.repliesTimelineRepository = repliesTimelineRepository;
        this.mediasTimelineRepository = mediasTimelineRepository;
        this.likesTimelineRepository = likesTimelineRepository;
    }

    public ITimelineStrategy getStrategy(String type) throws Exception {
        return switch (type.toUpperCase()) {
            case "FORYOU" -> foryouTimelineRepository;
            case "FOLLOWING" -> followingTimelineRepository;
            case "POSTS" -> postsTimelineRepository;
            case "REPLIES" -> repliesTimelineRepository;
            case "MEDIAS" -> mediasTimelineRepository;
            case "LIKES" -> likesTimelineRepository;
            default -> throw new Exception("Type not supported: " + type);
        };
    }
}
