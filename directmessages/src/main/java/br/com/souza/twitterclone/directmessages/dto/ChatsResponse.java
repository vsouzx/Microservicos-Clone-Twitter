package br.com.souza.twitterclone.directmessages.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChatsResponse {

    private String chatIdentifier;
    private String userIdentifier;
    private String userFirstName;
    private String userUsername;
    private Boolean isUserVerified;
    private String userProfilePhotoUrl;
    private String lastMessageText;
    private LocalDateTime lastMessageDate;
    private Boolean lastMessageSeen;
}
