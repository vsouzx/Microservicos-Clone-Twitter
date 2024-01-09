package br.comsouza.twitterclone.feed.database.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "tweets_favs")
public class TweetsFavs  implements Serializable {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "tweetIdentifier", column = @Column(name = "tweet_identifier", length = 36)),
            @AttributeOverride(name = "userIdentifier", column = @Column(name = "user_identifier", length = 36)),
    })
    private TweetsFavsId id;

    @Column(name = "time")
    private LocalDateTime time;

}
