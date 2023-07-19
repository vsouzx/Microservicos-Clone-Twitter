package br.com.souza.twitterclone.accounts.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class BlockedUsersId {

    @Column(name = "blocker_identifier", length = 36)
    private String blockerIdentifier;

    @Column(name = "blocked_identifier", length = 36)
    private String blockedIdentifier;

}
