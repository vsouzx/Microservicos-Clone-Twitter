package br.com.souza.twitterclone.notifications.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsByIdentifierResponse {

    private String userIdentifier;
    private String firstName;
    private String username;
    private Integer following;
    private Integer followers;
    private String biography;
    private String location;
    private String site;
    private Boolean privateAccount;
    private Boolean isBlockedByMe;
    private Boolean hasBlockedMe;
    private Boolean isFollowedByMe;
    private Boolean isPendingFollowedByMe;
    private Boolean isFollowingMe;
    private Boolean isSilencedByMe;
    private ProfilePhotoResponse profilePhoto;

}
