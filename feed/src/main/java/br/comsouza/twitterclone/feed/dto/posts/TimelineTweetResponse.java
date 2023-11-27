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
    private int tweetLikesCount;
    private int tweetViewsCount;
    private int tweetFavsCount;
    private boolean likedByMe;
    private boolean retweetedByMe;
    private boolean favoritedByMe;
    private String userProfilePhotoUrl;
    private List<byte[]> tweetAttachment;
    private boolean userIsVerified;

    public TimelineTweetResponse(Object[] result, IAmazonService iAmazonService) throws Exception {
        this.tweetIdentifier = (String) result[0];
        this.originalTweetIdentifier = (String) result[1];
        this.tweetTypeDescription = (String) result[2];
        this.userIdentifier = (String) result[3];
        this.userUsername = (String) result[4];
        this.userFirstName = (String) result[5];
        this.userProfilePhotoUrl = (String) result[6];
        this.tweetMessage = (String) result[7];
        this.tweetCommentsCount = (Integer) result[8];
        this.tweetRetweetsCount = (Integer) result[9];
        this.tweetLikesCount = (Integer) result[10];
        this.tweetViewsCount = (Integer) result[11];
        this.tweetFavsCount = (Integer) result[12];
        this.likedByMe = (boolean) result[13];
        this.favoritedByMe = (boolean) result[14];
        this.retweetedByMe = (boolean) result[15];
        this.tweetAttachment = result[16] != null && (boolean) result[16] ? iAmazonService.loadAttachmentFromS3((String) result[0]) : null;
        this.userIsVerified = (boolean) result[17];
    }
}
