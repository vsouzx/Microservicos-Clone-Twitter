package br.com.souza.twitterclone.directmessages.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class ChatMessagesReactionsId {

    @Column(name = "identifier", length = 36)
    private String identifier;

    @Column(name = "message_identifier", length = 36)
    private String messageIdentifier;

    @Column(name = "user_identifier", length = 36)
    private String userIdentifier;

}
