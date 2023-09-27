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

    private String userIdentifier;
    private String firstName;
    private String username;
    private String biography;
    private Boolean privateAccount;
    private Boolean isFollowedByMe;
    private Boolean isFollowingMe;
    private ProfilePhotoResponse profilePhoto;
    private ProfilePhotoResponse backgroundPhoto;

    public UserPreviewResponse(User user, IImagesRepository iImagesRepository) throws Exception {
        this.userIdentifier = user.getIdentifier();
        this.firstName = user.getFirstName();
        this.username = user.getUsername();
        this.biography = user.getBiography();
        this.privateAccount = user.getPrivateAccount();
        this.isFollowedByMe = false;
        this.isFollowingMe = false;
        this.profilePhoto = user.getProfilePhotoIdentifier() != null ? new ProfilePhotoResponse(iImagesRepository, user.getProfilePhotoIdentifier()) : null;
        this.backgroundPhoto = user.getBackgroundPhotoIdentifier() != null ? new ProfilePhotoResponse(iImagesRepository, user.getBackgroundPhotoIdentifier()) : null;
    }

}
