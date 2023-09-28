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
@Entity(name = "alerted_users")
@Builder
public class AlertedUsers {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "alerterIdentifier", column = @Column(name = "alerter_identifier", length = 36)),
            @AttributeOverride(name = "alertedIdentifier", column = @Column(name = "alerted_identifier", length = 36)),
    })
    private AlertedUsersId id;

}
