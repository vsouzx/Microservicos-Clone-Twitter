package br.comsouza.twitterclone.feed.database.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "tweets_likes")
public class TweetsLikes {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "tweetIdentifier", column = @Column(name = "tweet_identifier", length = 36)),
            @AttributeOverride(name = "userIdentifier", column = @Column(name = "user_identifier", length = 36)),
    })
    private TweetsLikesId id;

}
