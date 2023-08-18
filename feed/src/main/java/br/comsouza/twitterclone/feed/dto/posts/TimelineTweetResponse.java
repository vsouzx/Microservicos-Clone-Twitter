package br.comsouza.twitterclone.feed.dto.posts;

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
    private byte[] userProfilePhoto;
    private byte[] tweetAttachment;
}
