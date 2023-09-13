package br.com.souza.twitterclone.accounts.dto.user;

import java.time.LocalDateTime;
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

}
