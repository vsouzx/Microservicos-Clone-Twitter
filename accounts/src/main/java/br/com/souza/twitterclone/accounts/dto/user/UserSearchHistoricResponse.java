package br.com.souza.twitterclone.accounts.dto.user;

import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.model.UsersSearchHistoric;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchHistoricResponse {

    private String identifier;
    private String text;
    private UserMinimalizeResponse searchedUser;
    private LocalDateTime searchDate;

    public UserSearchHistoricResponse(UsersSearchHistoric historic, User searchedUser){
        this.identifier = historic.getIdentifier();
        this.text = historic.getText();
        this.searchedUser = UserMinimalizeResponse.builder()
                .userIdentifier(searchedUser.getIdentifier())
                .firstName(searchedUser.getFirstName())
                .username(searchedUser.getUsername())
                .profilePhotoUrl(searchedUser.getProfilePhotoUrl())
                .build();
        this.searchDate = historic.getSearchDate();
    }

}
