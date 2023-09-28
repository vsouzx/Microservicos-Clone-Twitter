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
import br.comsouza.twitterclone.feed.database.repository.postdetails.PostResumeRepository;
import br.comsouza.twitterclone.feed.dto.client.DeleteNotificationRequest;
import br.comsouza.twitterclone.feed.dto.client.UserDetailsByIdentifierResponse;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.enums.NotificationsTypeEnum;
import br.comsouza.twitterclone.feed.enums.TweetTypeEnum;
import br.comsouza.twitterclone.feed.handler.exceptions.InvalidTweetException;
import br.comsouza.twitterclone.feed.handler.exceptions.TweetNotFoundException;
import br.comsouza.twitterclone.feed.handler.exceptions.UnableToCommentException;
import br.comsouza.twitterclone.feed.handler.exceptions.UnableToLikeException;
import br.comsouza.twitterclone.feed.handler.exceptions.UnableToRetweetException;
import br.comsouza.twitterclone.feed.service.client.INotificationsClientService;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
import br.comsouza.twitterclone.feed.service.posts.IPostsMessageTranslatorService;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import br.comsouza.twitterclone.feed.service.tweettype.ITweetTypeService;
import br.comsouza.twitterclone.feed.util.UsefulDate;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class PostsServiceImpl implements IPostsService {

    private final ITweetsRepository tweetsRepository;
    private final ITweetTypeService iTweetTypeService;
    private final IInteractionsService iInteractionsService;
    private final ITweetsLikesRepository iTweetsLikesRepository;
    private final ITweetsFavsRepository iTweetsFavsRepository;
    private final IAccountsClient iAccountsClient;
    private final PostResumeRepository postResumeRepository;
    private final IPostsMessageTranslatorService messageTranslatorService;
    private final INotificationsClientService iNotificationsClientService;

    public PostsServiceImpl(ITweetsRepository tweetsRepository,
                            ITweetTypeService iTweetTypeService,
                            IInteractionsService iInteractionsService,
                            ITweetsLikesRepository iTweetsLikesRepository,
                            ITweetsFavsRepository iTweetsFavsRepository,
                            IAccountsClient iAccountsClient,
                            PostResumeRepository postResumeRepository,
                            IPostsMessageTranslatorService messageTranslatorService,
                            INotificationsClientService iNotificationsClientService) {
        this.tweetsRepository = tweetsRepository;
        this.iTweetTypeService = iTweetTypeService;
        this.iInteractionsService = iInteractionsService;
        this.iTweetsLikesRepository = iTweetsLikesRepository;
        this.iTweetsFavsRepository = iTweetsFavsRepository;
        this.iAccountsClient = iAccountsClient;
        this.postResumeRepository = postResumeRepository;
        this.messageTranslatorService = messageTranslatorService;
        this.iNotificationsClientService = iNotificationsClientService;
    }

    @Override
    public void postNewTweet(String message, String sessionUserIdentifier, MultipartFile attachment, String flag, String authorization) throws Exception {
        if ((message == null || message.isBlank()) && (attachment == null || attachment.isEmpty())) {
            throw new InvalidTweetException();
        }

        final String tweetIdentifier = UUID.randomUUID().toString();

        Tweets tweet = tweetsRepository.save(Tweets.builder()
                .tweetIdentifier(tweetIdentifier)
                .userIdentifier(sessionUserIdentifier)
                .originalTweetIdentifier(tweetIdentifier)
                .message(message)
                .messageTranslations(null)
                .type(iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.TWEET.toString()).getTypeIdentifier())
                .publicationTime(UsefulDate.now())
                .attachment(!attachment.isEmpty() ? attachment.getBytes() : null)
                .canBeRepliedByNotFollowedUser(flag.equals("1"))
                .build());

        messageTranslatorService.translateMessage(tweet, authorization);

        iNotificationsClientService.notifyAlerters(sessionUserIdentifier, NotificationsTypeEnum.NEW_POST.toString(), tweet.getTweetIdentifier(), authorization);
    }

    @Override
    public void retweetToggle(String message, String sessionUserIdentifier, MultipartFile attachment, String originalTweetIdentifier, String authorization) throws Exception {
        Tweets originalTweet = tweetsRepository.findById(originalTweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.getUserIdentifier(), authorization);
        if (tweetUserInfos == null) {
            throw new Exception("User info not found");
        }

        if (tweetUserInfos.getHasBlockedMe() || tweetUserInfos.getIsBlockedByMe() || tweetUserInfos.getPrivateAccount()) {
            throw new UnableToRetweetException();
        }

        String type = message == null && attachment.isEmpty()
                ? iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.NO_VALUE_RETWEET.toString()).getTypeIdentifier()
                : iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.RETWEET.toString()).getTypeIdentifier();

        Optional<Tweets> tweetsOptional = iInteractionsService.verifyIsRetweeted(originalTweetIdentifier, sessionUserIdentifier);

        if (tweetsOptional.isPresent()) {
            iNotificationsClientService.deleteNotification(
                    DeleteNotificationRequest.builder()
                            .tweetIdentifier(tweetsOptional.get().getTweetIdentifier())
                            .userSenderIdentifier(sessionUserIdentifier)
                            .userReceiverIdentifier(originalTweet.getUserIdentifier())
                            .typeDescription(NotificationsTypeEnum.NEW_RETWEET.toString())
                            .build(),
                    authorization
            );
            tweetsRepository.delete(tweetsOptional.get());
        }

        if (tweetsOptional.isEmpty()) {
            Tweets tweet = tweetsRepository.save(Tweets.builder()
                    .tweetIdentifier(UUID.randomUUID().toString())
                    .userIdentifier(sessionUserIdentifier)
                    .originalTweetIdentifier(originalTweetIdentifier)
                    .message(message)
                    .messageTranslations(null)
                    .type(type)
                    .publicationTime(UsefulDate.now())
                    .attachment(!attachment.isEmpty() ? attachment.getBytes() : null)
                    .canBeRepliedByNotFollowedUser(true)
                    .build());

            messageTranslatorService.translateMessage(tweet, authorization);

            iNotificationsClientService.createNewNotification(
                    sessionUserIdentifier,
                    originalTweet.getUserIdentifier(),
                    NotificationsTypeEnum.NEW_RETWEET.toString(),
                    tweet.getTweetIdentifier(),
                    authorization
            );
        }
        iInteractionsService.increaseViewsCount(originalTweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public void commentToggle(String message, String sessionUserIdentifier, MultipartFile attachment, String originalTweetIdentifier, String authorization) throws Exception {
        if ((message == null || message.isBlank()) && (attachment == null || attachment.isEmpty())) {
            throw new InvalidTweetException();
        }

        Tweets originalTweet = tweetsRepository.findById(originalTweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.getUserIdentifier(), authorization);
        if (tweetUserInfos == null) {
            throw new Exception("User info not found");
        }

        if (tweetUserInfos.getHasBlockedMe() || tweetUserInfos.getIsBlockedByMe()) {
            throw new UnableToCommentException();
        }
        if (tweetUserInfos.getPrivateAccount() && !tweetUserInfos.getIsFollowedByMe()) {
            throw new UnableToCommentException();
        }
        if (!originalTweet.getCanBeRepliedByNotFollowedUser() && !tweetUserInfos.getIsFollowingMe()) {
            throw new UnableToCommentException();
        }

        Optional<Tweets> optionalTweet = iInteractionsService.verifyIsCommented(originalTweetIdentifier, sessionUserIdentifier);

        if (optionalTweet.isPresent()) {
            iNotificationsClientService.deleteNotification(
                    DeleteNotificationRequest.builder()
                            .tweetIdentifier(optionalTweet.get().getTweetIdentifier())
                            .userSenderIdentifier(sessionUserIdentifier)
                            .userReceiverIdentifier(originalTweet.getUserIdentifier())
                            .typeDescription(NotificationsTypeEnum.NEW_COMMENT.toString())
                            .build(),
                    authorization);
            tweetsRepository.delete(optionalTweet.get());
        }

        if (optionalTweet.isEmpty()) {
            Tweets tweet = tweetsRepository.save(Tweets.builder()
                    .tweetIdentifier(UUID.randomUUID().toString())
                    .userIdentifier(sessionUserIdentifier)
                    .originalTweetIdentifier(originalTweetIdentifier)
                    .message(message)
                    .messageTranslations(null)
                    .type(iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.COMMENT.toString()).getTypeIdentifier())
                    .publicationTime(UsefulDate.now())
                    .attachment(!attachment.isEmpty() ? attachment.getBytes() : null)
                    .canBeRepliedByNotFollowedUser(true)
                    .build());

            messageTranslatorService.translateMessage(tweet, authorization);

            iNotificationsClientService.createNewNotification(
                    sessionUserIdentifier,
                    originalTweet.getUserIdentifier(),
                    NotificationsTypeEnum.NEW_COMMENT.toString(),
                    tweet.getTweetIdentifier(),
                    authorization
            );
        }
        iInteractionsService.increaseViewsCount(originalTweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public void likeToggle(String tweetIdentifier, String sessionUserIdentifier, String authorization) throws Exception {
        Tweets originalTweet = tweetsRepository.findById(tweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.getUserIdentifier(), authorization);
        if (tweetUserInfos == null) {
            throw new Exception("User info not found");
        }

        if (tweetUserInfos.getHasBlockedMe() || tweetUserInfos.getIsBlockedByMe()) {
            throw new UnableToLikeException();
        }
        if (tweetUserInfos.getPrivateAccount() && !tweetUserInfos.getIsFollowedByMe()) {
            throw new UnableToLikeException();
        }

        Optional<TweetsLikes> optionalTweetLike = iInteractionsService.verifyIsLiked(tweetIdentifier, sessionUserIdentifier);

        if (optionalTweetLike.isPresent()) {
            iNotificationsClientService.deleteNotification(
                    DeleteNotificationRequest.builder()
                            .tweetIdentifier(tweetIdentifier)
                            .userSenderIdentifier(sessionUserIdentifier)
                            .userReceiverIdentifier(originalTweet.getUserIdentifier())
                            .typeDescription(NotificationsTypeEnum.NEW_LIKE.toString())
                            .build(),
                    authorization);
            iTweetsLikesRepository.delete(optionalTweetLike.get());
        }

        if(optionalTweetLike.isEmpty()){
            iTweetsLikesRepository.save(TweetsLikes.builder()
                    .id(TweetsLikesId.builder()
                            .tweetIdentifier(tweetIdentifier)
                            .userIdentifier(sessionUserIdentifier)
                            .build())
                    .build());

            iNotificationsClientService.createNewNotification(
                    sessionUserIdentifier,
                    originalTweet.getUserIdentifier(),
                    NotificationsTypeEnum.NEW_LIKE.toString(),
                    tweetIdentifier,
                    authorization
            );
        }
        iInteractionsService.increaseViewsCount(tweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public void favToggle(String tweetIdentifier, String sessionUserIdentifier, String authorization) throws Exception {
        Tweets originalTweet = tweetsRepository.findById(tweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.getUserIdentifier(), authorization);
        if (tweetUserInfos == null) {
            throw new Exception("User info not found");
        }
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
                            .time(UsefulDate.now())
                            .build());
                });
        iInteractionsService.increaseViewsCount(tweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public void loadTweetResponses(TimelineTweetResponse post, String sessionUserIdentifier, String authorization) {
        TimelineTweetResponse originalTweet;

        post.setOriginalTweetResponse(getPostResumeByIdentifier(post, post, sessionUserIdentifier, false, authorization));
        originalTweet = post.getOriginalTweetResponse();
        if (originalTweet != null) {
            originalTweet.setOriginalTweetResponse(getPostResumeByIdentifier(post, originalTweet, sessionUserIdentifier, true, authorization));
        }
    }

    @Override
    public Integer getTweetsCount(String sessionUserIdentifier) {
        return tweetsRepository.findAllByUserIdentifier(sessionUserIdentifier).size();
    }

    private TimelineTweetResponse getPostResumeByIdentifier(TimelineTweetResponse mainTweet, TimelineTweetResponse secondaryTweet, String sessionUserIdentifier, boolean isThirdLevel, String authorization) {

        final String mainTweetType = mainTweet.getTweetTypeDescription();
        final String secondaryTweetType = secondaryTweet.getTweetTypeDescription();

        if (!mainTweetType.equals(TweetTypeEnum.TWEET.toString()) && !secondaryTweetType.equals(TweetTypeEnum.TWEET.toString())) {

            if (mainTweetType.equals(TweetTypeEnum.RETWEET.toString()) && !isThirdLevel) {
                return postResumeRepository.find(sessionUserIdentifier, secondaryTweet.getOriginalTweetIdentifier(), authorization);
            }
            if (mainTweetType.equals(TweetTypeEnum.NO_VALUE_RETWEET.toString()) || mainTweetType.equals(TweetTypeEnum.COMMENT.toString()) || secondaryTweet.getTweetTypeDescription().equals(TweetTypeEnum.COMMENT.toString())) {
                return postResumeRepository.find(sessionUserIdentifier, secondaryTweet.getOriginalTweetIdentifier(), authorization);
            }
        }
        return null;
    }
}
