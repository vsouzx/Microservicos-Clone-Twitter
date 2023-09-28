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
public class UserInfosUpdateRequest {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String biography;
    private String location;
    private String site;

}
