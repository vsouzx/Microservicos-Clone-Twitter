package br.com.souza.twitterclone.accounts.database.repository.followsdetails.factory;

import br.com.souza.twitterclone.accounts.database.repository.followsdetails.IFollowsDetailsStrategy;
import br.com.souza.twitterclone.accounts.database.repository.followsdetails.impl.UserFollowersRepository;
import br.com.souza.twitterclone.accounts.database.repository.followsdetails.impl.UserFollowingRepository;
import br.com.souza.twitterclone.accounts.database.repository.followsdetails.impl.UserKnownFollowersRepository;
import br.com.souza.twitterclone.accounts.database.repository.followsdetails.impl.UserVerifiedFollowersRepository;
import org.springframework.stereotype.Component;

@Component
public class FollowsDetailsStrategyFactory {

    private final UserFollowersRepository userFollowersRepository;
    private final UserFollowingRepository userFollowingRepository;
    private final UserKnownFollowersRepository userKnownFollowersRepository;
    private final UserVerifiedFollowersRepository userVerifiedFollowersRepository;

    public FollowsDetailsStrategyFactory(UserFollowersRepository userFollowersRepository,
                                         UserFollowingRepository userFollowingRepository,
                                         UserKnownFollowersRepository userKnownFollowersRepository,
                                         UserVerifiedFollowersRepository userVerifiedFollowersRepository) {
        this.userFollowersRepository = userFollowersRepository;
        this.userFollowingRepository = userFollowingRepository;
        this.userKnownFollowersRepository = userKnownFollowersRepository;
        this.userVerifiedFollowersRepository = userVerifiedFollowersRepository;
    }

    public IFollowsDetailsStrategy getStrategy(String type) throws Exception {
        return switch (type.toUpperCase()) {
            case "FOLLOWERS" -> userFollowersRepository;
            case "FOLLOWING" -> userFollowingRepository;
            case "KNOWN_FOLLOWERS" -> userKnownFollowersRepository;
            case "VERIFIED_FOLLOWERS" -> userVerifiedFollowersRepository;
            default -> throw new Exception("Type not supported: " + type);
        };
    }
}
