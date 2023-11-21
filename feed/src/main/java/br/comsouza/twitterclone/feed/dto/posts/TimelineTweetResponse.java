package br.comsouza.twitterclone.feed.dto.posts;

import br.comsouza.twitterclone.feed.service.aws.IAmazonService;
import br.comsouza.twitterclone.feed.service.interactions.IInteractionsService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimelineTweetResponse {

    private String tweetIdentifier;
    private String originalTweetIdentifier;
    private TimelineTweetResponse originalTweetResponse;
    private String tweetTypeDescription;
    private String userIdentifier;
    private String userUsername;
    private String userFirstName;
    private String tweetMessage;
    private List<TimelineTweetResponse> tweetCommentsList;
    private int tweetCommentsCount;
    private long tweetRetweetsCount;
    private long tweetNoValuesRetweetsCount;
    private int tweetLikesCount;
    private int tweetViewsCount;
    private int tweetFavsCount;
    private boolean isLikedByMe;
    private boolean isRetweetedByMe;
    private boolean isFavoritedByMe;
    private String userProfilePhotoUrl;
    private byte[] tweetAttachment;

    public TimelineTweetResponse(Object[] result, IAmazonService iAmazonService, IInteractionsService iInteractionsService, String sessionUserIdentifier) throws Exception {
        this.tweetIdentifier = (String) result[0];
        this.originalTweetIdentifier = (String) result[1];
        this.tweetTypeDescription = (String) result[2];
        this.userIdentifier = (String) result[3];
        this.userUsername = (String) result[4];
        this.userFirstName = (String) result[5];
        this.userProfilePhotoUrl = (String) result[6];
        this.tweetMessage = (String) result[7];
        this.tweetAttachment = iAmazonService.loadAttachmentFromS3((String) result[0]);
        this.tweetCommentsCount = iInteractionsService.getAllTweetCommentsCount((String) result[0]);
        this.tweetRetweetsCount = iInteractionsService.getTweetAllRetweetsTypesCount((String) result[0]);
        this.tweetLikesCount = iInteractionsService.getTweetLikesCount((String) result[0]);
        this.tweetViewsCount = iInteractionsService.getTweetViewsCount((String) result[0]);
        this.tweetFavsCount = iInteractionsService.getTweetFavsCount((String) result[0]);
        this.isLikedByMe = iInteractionsService.verifyIsLiked((String) result[0], sessionUserIdentifier).isPresent();
        this.isRetweetedByMe = iInteractionsService.verifyIsRetweeted((String) result[0], sessionUserIdentifier).isPresent();
    }
}
