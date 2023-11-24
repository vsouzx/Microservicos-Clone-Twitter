package br.comsouza.twitterclone.feed.service.interactions.impl;

import br.comsouza.twitterclone.feed.database.model.Tweets;
import br.comsouza.twitterclone.feed.database.model.TweetsFavs;
import br.comsouza.twitterclone.feed.database.model.TweetsLikes;
import br.comsouza.twitterclone.feed.database.model.TweetsTypes;
import br.comsouza.twitterclone.feed.database.model.TweetsViews;
import br.comsouza.twitterclone.feed.database.model.TweetsViewsId;
import br.comsouza.twitterclone.feed.database.repository.ITweetsFavsRepository;
import br.comsouza.twitterclone.feed.database.repository.ITweetsLikesRepository;
import br.comsouza.twitterclone.feed.database.repository.ITweetsRepository;
import br.comsouza.twitterclone.feed.database.repository.ITweetsViewsRepository;
import br.comsouza.twitterclone.feed.enums.TweetTypeEnum;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
import br.comsouza.twitterclone.feed.service.tweettype.ITweetTypeService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class InteractionsServiceImpl implements IInteractionsService {

    private final ITweetsRepository iTweetsRepository;
    private final ITweetsLikesRepository iTweetsLikesRepository;
    private final ITweetsViewsRepository iTweetsViewsRepository;
    private final ITweetsFavsRepository iTweetsFavsRepository;
    private final ITweetTypeService iTweetTypeService;

    public InteractionsServiceImpl(ITweetsRepository iTweetsRepository,
                                   ITweetsLikesRepository iTweetsLikesRepository,
                                   ITweetsViewsRepository iTweetsViewsRepository,
                                   ITweetsFavsRepository iTweetsFavsRepository,
                                   ITweetTypeService iTweetTypeService) {
        this.iTweetsRepository = iTweetsRepository;
        this.iTweetsLikesRepository = iTweetsLikesRepository;
        this.iTweetsViewsRepository = iTweetsViewsRepository;
        this.iTweetsFavsRepository = iTweetsFavsRepository;
        this.iTweetTypeService = iTweetTypeService;
    }

    @Override
    public Page<Tweets> getTweetCommentsPageable(String tweetIdentifier, Integer page, Integer size) {
        TweetsTypes tweetType = iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.COMMENT.toString());
        return iTweetsRepository.findAllByOriginalTweetIdentifierAndTypeInOrderByPublicationTimeDesc(tweetIdentifier, Collections.singletonList(tweetType.getTypeIdentifier()), PageRequest.of(page, size));
    }

    @Override
    public Page<Tweets> getTweetOnlyValuedRetweetsPageable(String tweetIdentifier, Integer page, Integer size) {
        List<String> retweetsTypes = new ArrayList<>();
        retweetsTypes.add(iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.RETWEET.toString()).getTypeIdentifier());
        return iTweetsRepository.findAllByOriginalTweetIdentifierAndTypeIn(tweetIdentifier, retweetsTypes, PageRequest.of(page, size));
    }

    @Override
    public Page<Tweets> getTweetOnlyNoValueRetweetsPageable(String tweetIdentifier, Integer page, Integer size) {
        List<String> retweetsTypes = new ArrayList<>();
        retweetsTypes.add(iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.NO_VALUE_RETWEET.toString()).getTypeIdentifier());
        return iTweetsRepository.findAllByOriginalTweetIdentifierAndTypeIn(tweetIdentifier, retweetsTypes, PageRequest.of(page, size));
    }

    @Override
    public Page<TweetsLikes> getTweetLikesPageable(String tweetIdentifier, Integer page, Integer size) {
        return iTweetsLikesRepository.findAllByIdTweetIdentifier(tweetIdentifier, PageRequest.of(page, size));
    }

    @Override
    public void increaseViewsCount(String tweetIdentifier, String userIdentifier) {
        iTweetsViewsRepository.save(TweetsViews.builder()
                .id(TweetsViewsId.builder()
                        .tweetIdentifier(tweetIdentifier)
                        .userIdentifier(userIdentifier)
                        .build())
                .build());
    }

    @Override
    public Optional<TweetsLikes> verifyIsLiked(String tweetIdentifier, String userIdentifier) {
        return iTweetsLikesRepository.findAllByIdTweetIdentifierAndIdUserIdentifier(tweetIdentifier, userIdentifier);
    }

    @Override
    public Optional<Tweets> verifyIsRetweeted(String tweetIdentifier, String userIdentifier) {
        TweetsTypes tweetType = iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.RETWEET.toString());
        Optional<Tweets> tweet = iTweetsRepository.findByUserIdentifierAndOriginalTweetIdentifierAndType(userIdentifier, tweetIdentifier, tweetType.getTypeIdentifier());

        if(tweet.isEmpty()){
            tweetType = iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.NO_VALUE_RETWEET.toString());
            tweet = iTweetsRepository.findByUserIdentifierAndOriginalTweetIdentifierAndType(userIdentifier, tweetIdentifier, tweetType.getTypeIdentifier());
        }
        return tweet;
    }

    @Override
    public Optional<Tweets> verifyIsCommented(String tweetIdentifier, String userIdentifier) {
        TweetsTypes tweetType = iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.COMMENT.toString());
        return iTweetsRepository.findByUserIdentifierAndOriginalTweetIdentifierAndType(userIdentifier, tweetIdentifier, tweetType.getTypeIdentifier());
    }

    @Override
    public Optional<TweetsFavs> verifyIsFavorited(String tweetIdentifier, String userIdentifier) {
        return iTweetsFavsRepository.findAllByIdTweetIdentifierAndIdUserIdentifier(tweetIdentifier, userIdentifier);
    }
}
