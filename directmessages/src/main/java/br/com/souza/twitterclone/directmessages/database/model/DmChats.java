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
@Entity(name = "dm_chats")
public class DmChats {

    @Id
    @Column(name = "identifier", length = 36)
    private String identifier;

    @Column(name = "user_identifier_1", length = 36)
    private String userIdentifier1;

    @Column(name = "user_identifier_2", length = 36)
    private String userIdentifier2;

}
