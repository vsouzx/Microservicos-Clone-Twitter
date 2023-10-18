package br.com.souza.twitterclone.directmessages.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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
@Embeddable
public class ChatIgnoredMessagesId {

    @Column(name = "chat_identifier", length = 36)
    private String chatIdentifier;

    @Column(name = "user_identifier", length = 36)
    private String userIdentifier;

    @Column(name = "message_identifier", length = 36)
    private String messageIdentifier;
}
