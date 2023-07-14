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
    private String lastName;
    private String username;
    private String email;
    private String biography;
    private String location;
    private String site;
    private LocalDateTime registrationTime;
}
