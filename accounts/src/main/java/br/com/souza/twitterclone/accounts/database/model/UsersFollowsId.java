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
public class UsersFollowsId {

    @Column(name = "follower_identifier", length = 36)
    private String followerIdentifier;

    @Column(name = "followed_identifier", length = 36)
    private String followedIdentifier;

}
