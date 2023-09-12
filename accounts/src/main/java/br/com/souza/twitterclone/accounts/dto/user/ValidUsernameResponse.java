package br.com.souza.twitterclone.accounts.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidUsernameResponse {

    private boolean isValidUsername;

}
