package br.com.souza.twitterclone.accounts.dto.user;

import br.com.souza.twitterclone.accounts.database.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreviewResponse {

    private String userIdentifier;
    private String firstName;
    private String username;
    private String biography;
    private Boolean privateAccount;
    private Boolean isFollowedByMe;
    private Boolean isFollowingMe;
    private String profilePhotoUrl;
    private Boolean isVerified;

    public UserPreviewResponse(User user) throws Exception {
        this.userIdentifier = user.getIdentifier();
        this.firstName = user.getFirstName();
        this.username = user.getUsername();
        this.biography = user.getBiography();
        this.privateAccount = user.getPrivateAccount();
        this.isFollowedByMe = false;
        this.isFollowingMe = false;
        this.profilePhotoUrl = user.getProfilePhotoUrl();
        this.isVerified = user.getVerified();
    }

}
