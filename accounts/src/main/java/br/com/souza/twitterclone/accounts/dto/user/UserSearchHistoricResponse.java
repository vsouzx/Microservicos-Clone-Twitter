package br.com.souza.twitterclone.accounts.dto.user;

import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.model.UsersSearchHistoric;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.accounts.service.user.IUserService;
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

    public UserSearchHistoricResponse(UsersSearchHistoric historic, IUserService iUserService) throws UserNotFoundException {
        this.identifier = historic.getIdentifier();
        this.text = historic.getText();
        this.searchDate = historic.getSearchDate();
        if(historic.getSearchedIdentifier() != null){
            User user = iUserService.findUserByUsernameOrEmailOrIdentifier(historic.getSearchedIdentifier());
            this.searchedUser = UserMinimalizeResponse.builder()
                    .userIdentifier(user.getIdentifier())
                    .firstName(user.getFirstName())
                    .username(user.getUsername())
                    .profilePhotoUrl(user.getProfilePhotoUrl())
                    .isVerified(user.getVerified())
                    .build();
        }
    }
}
