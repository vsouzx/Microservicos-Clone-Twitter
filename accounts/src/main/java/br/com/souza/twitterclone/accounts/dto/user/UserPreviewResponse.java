package br.com.souza.twitterclone.accounts.dto.user;

import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.repository.IImagesRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreviewResponse {

    private String firstName;
    private String username;
    private String biography;
    private Boolean privateAccount;
    private Boolean isFollowedByMe;
    private Boolean isFollowingMe;
    private ProfilePhotoResponse profilePhoto;

    public UserPreviewResponse(User user, IImagesRepository iImagesRepository) throws Exception {
        this.firstName = user.getFirstName();
        this.username = user.getUsername();
        this.biography = user.getBiography();
        this.privateAccount = user.getPrivateAccount();
        this.isFollowedByMe = false;
        this.isFollowingMe = false;
        this.profilePhoto = new ProfilePhotoResponse(iImagesRepository, user.getProfilePhotoIdentifier());
    }

}
