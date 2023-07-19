package br.com.souza.twitterclone.accounts.dto.user;

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
    private Integer follows;
    private Integer followers;
    private String biography;
    private String location;
    private String site;
    private LocalDateTime registrationTime;
    private Boolean privateAccount;
    private Boolean isBlockedByMe;
    private Boolean hasBlockedMe;
    private Boolean isFollowedByMe;
    private Boolean isFollowingMe;
    private List<UserDetailsResponse> followersInCommon;
    private byte[] profilePhoto;

}
