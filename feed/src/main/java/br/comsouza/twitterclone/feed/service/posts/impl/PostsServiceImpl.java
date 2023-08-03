package br.comsouza.twitterclone.feed.service.posts.impl;

import br.comsouza.twitterclone.feed.client.IAccountsClient;
import br.comsouza.twitterclone.feed.database.model.Tweets;
import br.comsouza.twitterclone.feed.database.model.TweetsFavs;
import br.comsouza.twitterclone.feed.database.model.TweetsFavsId;
import br.comsouza.twitterclone.feed.database.model.TweetsLikes;
import br.comsouza.twitterclone.feed.database.model.TweetsLikesId;
import br.comsouza.twitterclone.feed.database.repository.ITweetsFavsRepository;
import br.comsouza.twitterclone.feed.database.repository.ITweetsLikesRepository;
import br.comsouza.twitterclone.feed.database.repository.ITweetsRepository;
import br.comsouza.twitterclone.feed.database.repository.timeline.PostResumeRepository;
import br.comsouza.twitterclone.feed.dto.client.UserDetailsByIdentifierResponse;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.enums.TweetTypeEnum;
import br.comsouza.twitterclone.feed.handler.exceptions.InvalidTweetException;
import br.comsouza.twitterclone.feed.handler.exceptions.TweetNotFoundException;
import br.comsouza.twitterclone.feed.handler.exceptions.UnableToCommentException;
import br.comsouza.twitterclone.feed.handler.exceptions.UnableToLikeException;
import br.comsouza.twitterclone.feed.handler.exceptions.UnableToRetweetException;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import br.comsouza.twitterclone.feed.service.tweettype.ITweetTypeService;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostsServiceImpl implements IPostsService {

    private final ITweetsRepository tweetsRepository;
    private final ITweetTypeService iTweetTypeService;
    private final IInteractionsService iInteractionsService;
    private final ITweetsLikesRepository iTweetsLikesRepository;
    private final ITweetsFavsRepository iTweetsFavsRepository;
    private final IAccountsClient iAccountsClient;
    private final PostResumeRepository postResumeRepository;

    public PostsServiceImpl(ITweetsRepository tweetsRepository,
                            ITweetTypeService iTweetTypeService,
                            IInteractionsService iInteractionsService,
                            ITweetsLikesRepository iTweetsLikesRepository,
                            ITweetsFavsRepository iTweetsFavsRepository,
                            IAccountsClient iAccountsClient,
                            PostResumeRepository postResumeRepository) {
        this.tweetsRepository = tweetsRepository;
        this.iTweetTypeService = iTweetTypeService;
        this.iInteractionsService = iInteractionsService;
        this.iTweetsLikesRepository = iTweetsLikesRepository;
        this.iTweetsFavsRepository = iTweetsFavsRepository;
        this.iAccountsClient = iAccountsClient;
        this.postResumeRepository = postResumeRepository;
    }

    @Override
    public void postNewTweet(String message, String sessionUserIdentifier, MultipartFile attachment, String flag) throws Exception {
        final String tweetIdentifier = UUID.randomUUID().toString();

        if ((message == null || message.isBlank()) && (attachment == null || attachment.isEmpty())) {
            throw new InvalidTweetException();
        }

        tweetsRepository.save(Tweets.builder()
                .tweetIdentifier(tweetIdentifier)
                .userIdentifier(sessionUserIdentifier)
                .originalTweetIdentifier(tweetIdentifier)
                .message(message)
                .messageTranslations(null) // TODO: Fazer chamada ao chat GPT numa thread paralela
                .type(iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.TWEET.toString()).getTypeIdentifier())
                .publicationTime(LocalDateTime.now())
                .attachment(!attachment.isEmpty() ? attachment.getBytes() : null)
                .canBeRepliedByNotFollowedUser(flag.equals("1"))
                .build());
    }

    @Override
    public void retweetToggle(String message, String sessionUserIdentifier, MultipartFile attachment, String originalTweetIdentifier, String authorization) throws Exception {
        Optional<Tweets> originalTweet = tweetsRepository.findById(originalTweetIdentifier);

        if (originalTweet.isEmpty()) {
            throw new TweetNotFoundException();
        }

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.get().getUserIdentifier(), authorization);

        assert tweetUserInfos != null : "User info not found";

        if (tweetUserInfos.getHasBlockedMe() || tweetUserInfos.getIsBlockedByMe() || tweetUserInfos.getPrivateAccount()) {
            throw new UnableToRetweetException();
        }

        Optional<Tweets> retweet = iInteractionsService.verifyIsRetweeted(originalTweetIdentifier, sessionUserIdentifier);

        String type = message == null && attachment.isEmpty() ? iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.NO_VALUE_RETWEET.toString()).getTypeIdentifier() : iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.RETWEET.toString()).getTypeIdentifier();

        if (retweet.isPresent()) {
            tweetsRepository.delete(retweet.get());
        } else {
            tweetsRepository.save(Tweets.builder()
                    .tweetIdentifier(UUID.randomUUID().toString())
                    .userIdentifier(sessionUserIdentifier)
                    .originalTweetIdentifier(originalTweetIdentifier)
                    .message(message)
                    .messageTranslations(null) // TODO: Fazer chamada ao chat GPT numa thread paralela
                    .type(type)
                    .publicationTime(LocalDateTime.now())
                    .attachment(!attachment.isEmpty() ? attachment.getBytes() : null)
                    .canBeRepliedByNotFollowedUser(true)
                    .build());
        }
        iInteractionsService.increaseViewsCount(originalTweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public void commentToggle(String message, String sessionUserIdentifier, MultipartFile attachment, String originalTweetIdentifier, String authorization) throws Exception {
        Optional<Tweets> originalTweet = tweetsRepository.findById(originalTweetIdentifier);

        if (originalTweet.isEmpty()) {
            throw new TweetNotFoundException();
        }

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.get().getUserIdentifier(), authorization);

        assert tweetUserInfos != null : "User info not found";

        if (tweetUserInfos.getHasBlockedMe() || tweetUserInfos.getIsBlockedByMe()) {
            throw new UnableToCommentException();
        }

        if (tweetUserInfos.getPrivateAccount() && !tweetUserInfos.getIsFollowedByMe()) {
            throw new UnableToCommentException();
        }

        if (!originalTweet.get().getCanBeRepliedByNotFollowedUser() && !tweetUserInfos.getIsFollowingMe()) {
            throw new UnableToCommentException();
        }

        Optional<Tweets> retweet = iInteractionsService.verifyIsCommented(originalTweetIdentifier, sessionUserIdentifier);

        if (retweet.isPresent()) {
            tweetsRepository.delete(retweet.get());
        } else {
            tweetsRepository.save(Tweets.builder()
                    .tweetIdentifier(UUID.randomUUID().toString())
                    .userIdentifier(sessionUserIdentifier)
                    .originalTweetIdentifier(originalTweetIdentifier)
                    .message(message)
                    .messageTranslations(null) // TODO: Fazer chamada ao chat GPT numa thread paralela
                    .type(iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.COMMENT.toString()).getTypeIdentifier())
                    .publicationTime(LocalDateTime.now())
                    .attachment(!attachment.isEmpty() ? attachment.getBytes() : null)
                    .canBeRepliedByNotFollowedUser(true)
                    .build());
        }
        iInteractionsService.increaseViewsCount(originalTweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public void likeToggle(String tweetIdentifier, String sessionUserIdentifier, String authorization) throws Exception {
        Optional<Tweets> originalTweet = tweetsRepository.findById(tweetIdentifier);

        if (originalTweet.isEmpty()) {
            throw new TweetNotFoundException();
        }

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.get().getUserIdentifier(), authorization);

        assert tweetUserInfos != null : "User info not found";

        if (tweetUserInfos.getHasBlockedMe() || tweetUserInfos.getIsBlockedByMe()) {
            throw new UnableToLikeException();
        }

        if (tweetUserInfos.getPrivateAccount() && !tweetUserInfos.getIsFollowedByMe()) {
            throw new UnableToLikeException();
        }

        Optional<TweetsLikes> liked = iInteractionsService.verifyIsLiked(tweetIdentifier, sessionUserIdentifier);

        if (liked.isPresent()) {
            iTweetsLikesRepository.delete(liked.get());
        } else {
            iTweetsLikesRepository.save(TweetsLikes.builder()
                    .id(TweetsLikesId.builder()
                            .tweetIdentifier(tweetIdentifier)
                            .userIdentifier(sessionUserIdentifier)
                            .build())
                    .build());
        }
        iInteractionsService.increaseViewsCount(tweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public void favToggle(String tweetIdentifier, String sessionUserIdentifier, String authorization) throws Exception {
        Optional<Tweets> originalTweet = tweetsRepository.findById(tweetIdentifier);

        //TODO: adicionar lógica para ver se o dono do tweet original não bloqueou o session user ou vice versa
        if (originalTweet.isEmpty()) {
            throw new TweetNotFoundException();
        }

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.get().getUserIdentifier(), authorization);

        assert tweetUserInfos != null : "User info not found";

        if (tweetUserInfos.getHasBlockedMe() || tweetUserInfos.getIsBlockedByMe()) {
            throw new UnableToLikeException();
        }

        if (tweetUserInfos.getPrivateAccount() && !tweetUserInfos.getIsFollowedByMe()) {
            throw new UnableToLikeException();
        }

        Optional<TweetsFavs> fav = iInteractionsService.verifyIsFavorited(tweetIdentifier, sessionUserIdentifier);

        if (fav.isPresent()) {
            iTweetsFavsRepository.delete(fav.get());
        } else {
            iTweetsFavsRepository.save(TweetsFavs.builder()
                    .id(TweetsFavsId.builder()
                            .tweetIdentifier(tweetIdentifier)
                            .userIdentifier(sessionUserIdentifier)
                            .time(LocalDateTime.now())
                            .build())
                    .build());
        }
        iInteractionsService.increaseViewsCount(tweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public TimelineTweetResponse getPostResumeByIdentifier(TimelineTweetResponse referenceTweet, TimelineTweetResponse secondTweet, String sessionUserIdentifier, boolean isThirdLevel) throws Exception {

        //se o tweet referência for do tipo TWEET, retorna null
        if (!referenceTweet.getTweetTypeDescription().equals(TweetTypeEnum.TWEET.toString())) {

            //se o tweet referência for do tipo RETWEET, carrega o segundo tweet
            if (referenceTweet.getTweetTypeDescription().equals(TweetTypeEnum.RETWEET.toString()) && !isThirdLevel) {
                return postResumeRepository.find(sessionUserIdentifier, secondTweet.getOriginalTweetIdentifier());
            }

            //se o tweet referência for do tipo NO_VALUE_RETWEET OU COMMENT, carrega o terceiro tweet
            if (referenceTweet.getTweetTypeDescription().equals(TweetTypeEnum.NO_VALUE_RETWEET.toString()) || referenceTweet.getTweetTypeDescription().equals(TweetTypeEnum.COMMENT.toString())) {
                return postResumeRepository.find(sessionUserIdentifier, secondTweet.getOriginalTweetIdentifier());
            }
        }
        return null;
    }
}
