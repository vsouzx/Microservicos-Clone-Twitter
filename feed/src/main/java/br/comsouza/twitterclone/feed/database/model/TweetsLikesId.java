package br.comsouza.twitterclone.feed.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class TweetsLikesId  implements Serializable {

    @Column(name = "tweet_identifier", length = 36)
    private String tweetIdentifier;

    @Column(name = "user_identifier", length = 36)
    private String userIdentifier;

}
