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
@Entity(name = "users_follows")
@Builder
public class UsersFollows {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "followerIdentifier", column = @Column(name = "follower_identifier", length = 36)),
            @AttributeOverride(name = "followedIdentifier", column = @Column(name = "followed_identifier", length = 36)),
    })
    private UsersFollowsId id;

}
