package br.com.souza.twitterclone.notifications.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "notifications")
public class Notifications {

    @Id
    @Column(name = "identifier", length = 36)
    private String identifier;

    @Column(name = "tweet_identifier", length = 50)
    private String tweetIdentifier;

    @Column(name = "user_sender_identifier", length = 36)
    private String userSenderIdentifier;

    @Column(name = "user_receiver_identifier", length = 36)
    private String userReceiverIdentifier;

    @Column(name = "type_identifier", length = 36)
    private String typeIdentifier;

    @Column(name = "visualized")
    private Boolean visualized;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

}
