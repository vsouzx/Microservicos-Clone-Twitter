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
public class UserDetailsResponse {

    private String firstName;
    private String username;
    private Integer following;
    private Integer followers;
    private String biography;
    private String location;
    private String site;
    private LocalDateTime registrationTime;
    private Boolean privateAccount;
    private String languagePreference;
    private Integer tweetsCount;
    private String profilePhotoUrl;
    private String backgroundPhotoUrl;
    private Boolean isVerified;

}
