package br.com.souza.twitterclone.notifications.dto.notifications;

import br.com.souza.twitterclone.notifications.dto.client.TimelineTweetResponse;
import br.com.souza.twitterclone.notifications.dto.client.UserDetailsByIdentifierResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationsResponse {

    private String notificationIdentifier;
    private String typeDescription;
    private TimelineTweetResponse tweetResponse;
    private UserDetailsByIdentifierResponse userResponse;

}
