package br.comsouza.twitterclone.feed.dto.favs;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavTweetResponse {

    private String tweetIdentifier;
    private String originalTweetIdentifier;
    private FavTweetResponse originalTweetResponse;
    private String tweetTypeDescription;
    private String userIdentifier;
    private String userUsername;
    private String userFirstName;
    private String tweetMessage;
    private int tweetCommentsCount;
    private int tweetRetweetsCount;
    private int tweetLikesCount;
    private int tweetViewsCount;
    private byte[] userProfilePhoto;
    private byte[] tweetAttachment;
    private LocalDateTime favTime;
}
