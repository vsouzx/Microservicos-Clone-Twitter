package br.comsouza.twitterclone.feed.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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
@Embeddable
public class TweetsViewsId implements Serializable {

    @Column(name = "tweet_identifier", length = 36)
    private String tweetIdentifier;

    @Column(name = "user_identifier", length = 36)
    private String userIdentifier;

    @Column(name = "time")
    private LocalDateTime time;

}
