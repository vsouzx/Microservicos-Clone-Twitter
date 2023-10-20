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
@Entity(name = "chat_messages_reactions")
public class ChatMessagesReactions {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "identifier", column = @Column(name = "identifier", length = 36)),
            @AttributeOverride(name = "messageIdentifier", column = @Column(name = "message_identifier", length = 36)),
            @AttributeOverride(name = "userIdentifier", column = @Column(name = "user_identifier", length = 36))
    })
    private ChatMessagesReactionsId id;

    @Column(name = "emoji")
    private String emoji;
}
