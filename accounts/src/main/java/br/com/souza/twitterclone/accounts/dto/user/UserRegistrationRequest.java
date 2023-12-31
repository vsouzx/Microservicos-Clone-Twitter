package br.com.souza.twitterclone.accounts.dto.user;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationRequest {

    @NotNull
    private String firstName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private LocalDate birthDate;
    @NotNull
    private String confirmationCode;

}
