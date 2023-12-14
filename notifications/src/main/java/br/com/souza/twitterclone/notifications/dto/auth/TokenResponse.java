package br.com.souza.twitterclone.notifications.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponse {

    private String token;
    private Long expiresIn;
    private Boolean firstAccess;
    private String username;

}
