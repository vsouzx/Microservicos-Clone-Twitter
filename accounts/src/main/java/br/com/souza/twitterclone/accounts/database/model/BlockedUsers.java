package br.com.souza.twitterclone.accounts.database.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "blocked_users")
@Builder
public class BlockedUsers {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "blockerIdentifier", column = @Column(name = "blocker_identifier", length = 36)),
            @AttributeOverride(name = "blockedIdentifier", column = @Column(name = "blocked_identifier", length = 36)),
    })
    private BlockedUsersId id;

}
