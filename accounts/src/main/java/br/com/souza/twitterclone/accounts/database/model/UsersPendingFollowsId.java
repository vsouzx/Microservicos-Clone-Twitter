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
public class UsersPendingFollowsId {

    @Column(name = "pending_follower_identifier", length = 36)
    private String pendingFollowerIdentifier;

    @Column(name = "pending_followed_identifier", length = 36)
    private String pendingFollowedIdentifier;

}
