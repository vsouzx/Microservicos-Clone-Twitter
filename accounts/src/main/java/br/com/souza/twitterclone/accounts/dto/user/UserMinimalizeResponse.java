package br.com.souza.twitterclone.accounts.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMinimalizeResponse {

    private String userIdentifier;
    private String firstName;
    private String username;
    private String profilePhotoUrl;

}
