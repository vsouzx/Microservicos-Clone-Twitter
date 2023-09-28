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
public class AlertedUsersId {

    @Column(name = "alerter_identifier", length = 36)
    private String alerterIdentifier;

    @Column(name = "alerted_identifier", length = 36)
    private String alertedIdentifier;

}
