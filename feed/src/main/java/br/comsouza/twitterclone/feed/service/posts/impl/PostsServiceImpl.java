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
        Tweets originalTweet = tweetsRepository.findById(originalTweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.getUserIdentifier(), authorization);
        assert tweetUserInfos != null : "User info not found";

        if (tweetUserInfos.getHasBlockedMe() || tweetUserInfos.getIsBlockedByMe() || tweetUserInfos.getPrivateAccount()) {
            throw new UnableToRetweetException();
        }

        String type = message == null && attachment.isEmpty()
                ? iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.NO_VALUE_RETWEET.toString()).getTypeIdentifier()
                : iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.RETWEET.toString()).getTypeIdentifier();

        iInteractionsService.verifyIsRetweeted(originalTweetIdentifier, sessionUserIdentifier)
                .ifPresentOrElse(tweetsRepository::delete, () -> {
                    try{
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
                    }catch (Exception e){
                        throw new RuntimeException();
                    }
                });

        iInteractionsService.increaseViewsCount(originalTweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public void commentToggle(String message, String sessionUserIdentifier, MultipartFile attachment, String originalTweetIdentifier, String authorization) throws Exception {
        Tweets originalTweet = tweetsRepository.findById(originalTweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.getUserIdentifier(), authorization);

        assert tweetUserInfos != null : "User info not found";

        if (tweetUserInfos.getHasBlockedMe() || tweetUserInfos.getIsBlockedByMe()) {
            throw new UnableToCommentException();
        }

        if (tweetUserInfos.getPrivateAccount() && !tweetUserInfos.getIsFollowedByMe()) {
            throw new UnableToCommentException();
        }

        if (!originalTweet.getCanBeRepliedByNotFollowedUser() && !tweetUserInfos.getIsFollowingMe()) {
            throw new UnableToCommentException();
        }

        iInteractionsService.verifyIsCommented(originalTweetIdentifier, sessionUserIdentifier)
                .ifPresentOrElse(tweetsRepository::delete, () -> {
                    try {
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
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        iInteractionsService.increaseViewsCount(originalTweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public void likeToggle(String tweetIdentifier, String sessionUserIdentifier, String authorization) throws Exception {
        Tweets originalTweet = tweetsRepository.findById(tweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.getUserIdentifier(), authorization);

        assert tweetUserInfos != null : "User info not found";

        if (tweetUserInfos.getHasBlockedMe() || tweetUserInfos.getIsBlockedByMe()) {
            throw new UnableToLikeException();
        }

        if (tweetUserInfos.getPrivateAccount() && !tweetUserInfos.getIsFollowedByMe()) {
            throw new UnableToLikeException();
        }

        iInteractionsService.verifyIsLiked(tweetIdentifier, sessionUserIdentifier)
                .ifPresentOrElse(iTweetsLikesRepository::delete, () -> {
                    iTweetsLikesRepository.save(TweetsLikes.builder()
                            .id(TweetsLikesId.builder()
                                    .tweetIdentifier(tweetIdentifier)
                                    .userIdentifier(sessionUserIdentifier)
                                    .build())
                            .build());
                });

        iInteractionsService.increaseViewsCount(tweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public void favToggle(String tweetIdentifier, String sessionUserIdentifier, String authorization) throws Exception {
        Tweets originalTweet = tweetsRepository.findById(tweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.getUserIdentifier(), authorization);

        assert tweetUserInfos != null : "User info not found";

        if (tweetUserInfos.getHasBlockedMe() || tweetUserInfos.getIsBlockedByMe()) {
            throw new UnableToLikeException();
        }

        if (tweetUserInfos.getPrivateAccount() && !tweetUserInfos.getIsFollowedByMe()) {
            throw new UnableToLikeException();
        }

        iInteractionsService.verifyIsFavorited(tweetIdentifier, sessionUserIdentifier)
                .ifPresentOrElse(iTweetsFavsRepository::delete, () -> {
                    iTweetsFavsRepository.save(TweetsFavs.builder()
                            .id(TweetsFavsId.builder()
                                    .tweetIdentifier(tweetIdentifier)
                                    .userIdentifier(sessionUserIdentifier)
                                    .build())
                            .time(LocalDateTime.now())
                            .build());
                });

        iInteractionsService.increaseViewsCount(tweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public void loadTweetResponses(TimelineTweetResponse post, String sessionUserIdentifier) throws Exception {
        TimelineTweetResponse originalTweet;

        post.setOriginalTweetResponse(getPostResumeByIdentifier(post, post, sessionUserIdentifier, false));
        originalTweet = post.getOriginalTweetResponse();
        if (originalTweet != null) {
            originalTweet.setOriginalTweetResponse(getPostResumeByIdentifier(post, originalTweet, sessionUserIdentifier, true));
        }
    }

    private TimelineTweetResponse getPostResumeByIdentifier(TimelineTweetResponse referenceTweet, TimelineTweetResponse secondTweet, String sessionUserIdentifier, boolean isThirdLevel) throws Exception {

        final String referenceTweetType = referenceTweet.getTweetTypeDescription();
        final String secondTweetType = secondTweet.getTweetTypeDescription();

        if (!referenceTweetType.equals(TweetTypeEnum.TWEET.toString()) && !secondTweetType.equals(TweetTypeEnum.TWEET.toString())) {

            if (referenceTweetType.equals(TweetTypeEnum.RETWEET.toString()) && !isThirdLevel) {
                return postResumeRepository.find(sessionUserIdentifier, secondTweet.getOriginalTweetIdentifier());
            }
            if (referenceTweetType.equals(TweetTypeEnum.NO_VALUE_RETWEET.toString()) || referenceTweetType.equals(TweetTypeEnum.COMMENT.toString()) || secondTweet.getTweetTypeDescription().equals(TweetTypeEnum.COMMENT.toString())) {
                return postResumeRepository.find(sessionUserIdentifier, secondTweet.getOriginalTweetIdentifier());
            }
        }
        return null;
    }


}
