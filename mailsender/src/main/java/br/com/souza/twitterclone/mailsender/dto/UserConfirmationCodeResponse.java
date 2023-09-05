package br.com.souza.twitterclone.mailsender.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserConfirmationCodeResponse {

    private String email;
    private String confirmationCode;
}
