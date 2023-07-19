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
@Entity(name = "users_pending_follows")
@Builder
public class UsersPendingFollows {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "pendingFollowerIdentifier", column = @Column(name = "pending_follower_identifier", length = 36)),
            @AttributeOverride(name = "pendingFollowedIdentifier", column = @Column(name = "pending_followed_identifier", length = 36)),
    })
    private UsersPendingFollowsId id;

}
