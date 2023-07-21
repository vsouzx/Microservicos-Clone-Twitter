package br.com.souza.twitterclone.accounts.database.model;

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
@Entity(name = "silenced_users")
@Builder
public class SilencedUsers {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "silencerIdentifier", column = @Column(name = "silencer_identifier", length = 36)),
            @AttributeOverride(name = "silencedIdentifier", column = @Column(name = "silenced_identifier", length = 36)),
    })
    private SilencedUsersId id;

}
