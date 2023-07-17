package br.com.souza.twitterclone.accounts.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPasswordUpdateRequest {

    @NotNull
    private String actualPassword;
    @NotNull
    private String newPassword;
    @NotNull
    private String newPasswordConfirmation;

}
