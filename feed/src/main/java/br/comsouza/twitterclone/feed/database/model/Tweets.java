package br.comsouza.twitterclone.feed.database.model;

import jakarta.persistence.Column;
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
@Entity(name = "tweets")
public class Tweets {

    @Id
    @Column(name = "tweet_identifier", length = 36)
    private String tweetIdentifier;

    @Column(name = "user_identifier", length = 36)
    private String userIdentifier;

    @Column(name = "original_tweet_identifier", length = 36)
    private String originalTweetIdentifier;

    @Column(name = "message", length = 255)
    private String message;

    @Column(name = "message_translations", length = 255)
    private String messageTranslations;

    @Column(name = "type", length = 36)
    private String type;

    @Column(name = "publication_time", length = 36)
    private String publicationTime;

    @Lob
    @Column(name = "attachment")
    private byte[] attachment;
}
