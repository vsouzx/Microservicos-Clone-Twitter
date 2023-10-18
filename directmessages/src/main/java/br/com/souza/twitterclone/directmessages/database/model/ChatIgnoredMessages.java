package br.com.souza.twitterclone.directmessages.database.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "chat_ignored_messages")

public class ChatIgnoredMessages {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "userIdentifier", column = @Column(name = "user_identifier", length = 36)),
            @AttributeOverride(name = "chatIdentifier", column = @Column(name = "chat_identifier", length = 36)),
            @AttributeOverride(name = "messageIdentifier", column = @Column(name = "message_identifier", length = 36))
    })
   private ChatIgnoredMessagesId id;
}
