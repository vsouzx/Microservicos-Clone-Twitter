package br.com.souza.twitterclone.accounts.database.model;

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
public class SilencedUsersId {

    @Column(name = "silencer_identifier", length = 36)
    private String silencerIdentifier;

    @Column(name = "silenced_identifier", length = 36)
    private String silencedIdentifier;

}
