package br.comsouza.twitterclone.feed.dto.client;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsByIdentifierResponse {

    private String firstName;
    private String lastName;
    private String username;
    private Integer following;
    private Integer followers;
    private String biography;
    private String location;
    private String site;
    private LocalDateTime registrationTime;
    private Boolean privateAccount;
    private Boolean isBlockedByMe;
    private Boolean hasBlockedMe;
    private Boolean isFollowedByMe;
    private Boolean isPendingFollowedByMe;
    private Boolean isFollowingMe;
    private Boolean isSilencedByMe;
    private byte[] profilePhoto;

}
