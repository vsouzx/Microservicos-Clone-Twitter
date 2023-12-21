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
import br.comsouza.twitterclone.feed.handler.exceptions.InvalidAttachmentException;
import br.comsouza.twitterclone.feed.handler.exceptions.InvalidTweetException;
import br.comsouza.twitterclone.feed.handler.exceptions.TweetNotFoundException;
import br.comsouza.twitterclone.feed.handler.exceptions.UnableToCommentException;
import br.comsouza.twitterclone.feed.handler.exceptions.UnableToLikeException;
import br.comsouza.twitterclone.feed.handler.exceptions.UnableToRetweetException;
import br.comsouza.twitterclone.feed.service.aws.IAmazonService;
import br.comsouza.twitterclone.feed.service.client.INotificationsClientService;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
import br.comsouza.twitterclone.feed.service.posts.IPostsMessageTranslatorService;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import br.comsouza.twitterclone.feed.service.tweettype.ITweetTypeService;
import br.comsouza.twitterclone.feed.util.UsefulDate;
import com.amazonaws.services.s3.AmazonS3;
import java.util.List;
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
    private final IAmazonService iAmazonService;

    public PostsServiceImpl(ITweetsRepository tweetsRepository,
                            ITweetTypeService iTweetTypeService,
                            IInteractionsService iInteractionsService,
                            ITweetsLikesRepository iTweetsLikesRepository,
                            ITweetsFavsRepository iTweetsFavsRepository,
                            IAccountsClient iAccountsClient,
                            PostResumeRepository postResumeRepository,
                            IPostsMessageTranslatorService messageTranslatorService,
                            INotificationsClientService iNotificationsClientService,
                            AmazonS3 s3client,
                            IAmazonService iAmazonService) {
        this.tweetsRepository = tweetsRepository;
        this.iTweetTypeService = iTweetTypeService;
        this.iInteractionsService = iInteractionsService;
        this.iTweetsLikesRepository = iTweetsLikesRepository;
        this.iTweetsFavsRepository = iTweetsFavsRepository;
        this.iAccountsClient = iAccountsClient;
        this.postResumeRepository = postResumeRepository;
        this.messageTranslatorService = messageTranslatorService;
        this.iNotificationsClientService = iNotificationsClientService;
        this.iAmazonService = iAmazonService;
    }

    @Override
    public void postNewTweet(String message, String sessionUserIdentifier, List<MultipartFile> attachments, String flag) throws Exception {
        if ((message == null || message.isBlank()) && (attachments == null)) {
            throw new InvalidTweetException();
        }

        if (attachments != null && attachments.size() > 4) {
            throw new InvalidAttachmentException();
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
                .canBeRepliedByNotFollowedUser(flag.equals("1"))
                .hasAttachment(attachments != null)
                .build());

        if (attachments != null) {
            iAmazonService.saveAttachmentInBucketS3(attachments, tweetIdentifier);
        }
        messageTranslatorService.translateMessage(tweet);
        iNotificationsClientService.notifyAlerters(sessionUserIdentifier, NotificationsTypeEnum.NEW_POST.toString(), tweet.getTweetIdentifier());
    }

    @Override
    public void retweetToggle(String message, String sessionUserIdentifier, List<MultipartFile> attachments, String originalTweetIdentifier) throws Exception {
        Tweets originalTweet = tweetsRepository.findById(originalTweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.getUserIdentifier());
        if (tweetUserInfos == null) {
            throw new Exception("User info not found");
        }

        if (tweetUserInfos.getHasBlockedMe() || tweetUserInfos.getIsBlockedByMe() || tweetUserInfos.getPrivateAccount()) {
            throw new UnableToRetweetException();
        }

        if (attachments != null && attachments.size() > 4) {
            throw new InvalidAttachmentException();
        }

        String noValuesRetweetTypeIdentifier = iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.NO_VALUE_RETWEET.name()).getTypeIdentifier();
        String retweetTypeIdentifier = iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.RETWEET.name()).getTypeIdentifier();

        String type = message == null && (attachments == null)
                ? noValuesRetweetTypeIdentifier
                : retweetTypeIdentifier;

        Optional<Tweets> tweetsOptional = Optional.empty();
        if(type.equals(noValuesRetweetTypeIdentifier)){
            tweetsOptional = iInteractionsService.verifyIsNoValueRetweeted(originalTweetIdentifier, sessionUserIdentifier);
        }

        if (tweetsOptional.isPresent()) {
            iNotificationsClientService.deleteNotification(
                    DeleteNotificationRequest.builder()
                            .tweetIdentifier(tweetsOptional.get().getTweetIdentifier())
                            .userSenderIdentifier(sessionUserIdentifier)
                            .userReceiverIdentifier(originalTweet.getUserIdentifier())
                            .typeDescription(NotificationsTypeEnum.NEW_RETWEET.toString())
                            .build()
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
                    .canBeRepliedByNotFollowedUser(true)
                    .hasAttachment(attachments != null)
                    .build());

            if (attachments != null) {
                iAmazonService.saveAttachmentInBucketS3(attachments, tweet.getTweetIdentifier());
            }

            messageTranslatorService.translateMessage(tweet);

            iNotificationsClientService.createNewNotification(
                    sessionUserIdentifier,
                    originalTweet.getUserIdentifier(),
                    NotificationsTypeEnum.NEW_RETWEET.toString(),
                    tweet.getTweetIdentifier()
            );
        }
        iInteractionsService.increaseViewsCount(originalTweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public void commentToggle(String message, String sessionUserIdentifier, List<MultipartFile> attachments, String originalTweetIdentifier) throws Exception {
        if ((message == null || message.isBlank()) && (attachments == null)) {
            throw new InvalidTweetException();
        }

        if (attachments != null && attachments.size() > 4) {
            throw new InvalidAttachmentException();
        }

        Tweets originalTweet = tweetsRepository.findById(originalTweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        if(!originalTweet.getUserIdentifier().equals(sessionUserIdentifier)){
            UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.getUserIdentifier());
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
        }

        Tweets tweet = tweetsRepository.save(Tweets.builder()
                .tweetIdentifier(UUID.randomUUID().toString())
                .userIdentifier(sessionUserIdentifier)
                .originalTweetIdentifier(originalTweetIdentifier)
                .message(message)
                .messageTranslations(null)
                .type(iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.COMMENT.toString()).getTypeIdentifier())
                .publicationTime(UsefulDate.now())
                .canBeRepliedByNotFollowedUser(true)
                .hasAttachment(attachments != null)
                .build());

        if (attachments != null) {
            iAmazonService.saveAttachmentInBucketS3(attachments, tweet.getTweetIdentifier());
        }

        messageTranslatorService.translateMessage(tweet);

        iNotificationsClientService.createNewNotification(
                sessionUserIdentifier,
                originalTweet.getUserIdentifier(),
                NotificationsTypeEnum.NEW_COMMENT.toString(),
                tweet.getTweetIdentifier()
        );

        iInteractionsService.increaseViewsCount(originalTweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public void likeToggle(String tweetIdentifier, String sessionUserIdentifier) throws Exception {
        Tweets originalTweet = tweetsRepository.findById(tweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.getUserIdentifier());
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
                            .build());
            iTweetsLikesRepository.delete(optionalTweetLike.get());
        }

        if (optionalTweetLike.isEmpty()) {
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
                    tweetIdentifier
            );
        }
        iInteractionsService.increaseViewsCount(tweetIdentifier, sessionUserIdentifier);
    }

    @Override
    public void favToggle(String tweetIdentifier, String sessionUserIdentifier) throws Exception {
        Tweets originalTweet = tweetsRepository.findById(tweetIdentifier)
                .orElseThrow(TweetNotFoundException::new);

        UserDetailsByIdentifierResponse tweetUserInfos = iAccountsClient.getUserInfosByIdentifier(originalTweet.getUserIdentifier());
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
    public void loadTweetResponses(TimelineTweetResponse post, String sessionUserIdentifier) throws Exception {
        TimelineTweetResponse originalTweet;

        post.setOriginalTweetResponse(getPostResumeByIdentifier(post, post, sessionUserIdentifier, false));
        originalTweet = post.getOriginalTweetResponse();
        if (originalTweet != null) {
            originalTweet.setOriginalTweetResponse(getPostResumeByIdentifier(post, originalTweet, sessionUserIdentifier, true));
        }
    }

    @Override
    public Integer getTweetsCount(String sessionUserIdentifier) {
        return tweetsRepository.findAllByUserIdentifier(sessionUserIdentifier).size();
    }

    private TimelineTweetResponse getPostResumeByIdentifier(TimelineTweetResponse mainTweet, TimelineTweetResponse secondaryTweet, String sessionUserIdentifier, boolean isThirdLevel) throws Exception {

        final String mainTweetType = mainTweet.getTweetTypeDescription();
        final String secondaryTweetType = secondaryTweet.getTweetTypeDescription();

        if (!mainTweetType.equals(TweetTypeEnum.TWEET.toString()) && !secondaryTweetType.equals(TweetTypeEnum.TWEET.toString())) {
            if (mainTweetType.equals(TweetTypeEnum.RETWEET.toString()) && !isThirdLevel) {
                return postResumeRepository.find(sessionUserIdentifier, secondaryTweet.getOriginalTweetIdentifier());
            }
            if (mainTweetType.equals(TweetTypeEnum.NO_VALUE_RETWEET.toString()) || mainTweetType.equals(TweetTypeEnum.COMMENT.toString()) || secondaryTweet.getTweetTypeDescription().equals(TweetTypeEnum.COMMENT.toString())) {
                return postResumeRepository.find(sessionUserIdentifier, secondaryTweet.getOriginalTweetIdentifier());
            }
        }
        return null;
    }

}
