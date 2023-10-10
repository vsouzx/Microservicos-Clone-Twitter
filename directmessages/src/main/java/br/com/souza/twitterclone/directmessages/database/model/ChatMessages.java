package br.com.souza.twitterclone.directmessages.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "chat_messages")
public class ChatMessages {

    @Id
    @Column(name = "identifier", length = 36)
    private String identifier;

    @Column(name = "chat_identifier", length = 36)
    private String chatIdentifier;

    @Column(name = "user_identifier", length = 36)
    private String userIdentifier;

    @Column(name = "text")
    private String text;

    @Column(name = "tweet_identifier", length = 36)
    private String tweetIdentifier;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "emoji")
    private String emoji;

    @Column(name = "seen")
    private Boolean seen;
}
