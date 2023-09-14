package br.com.souza.twitterclone.notifications.dto.notifications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NewNotificationRequest {

    private String tweetIdentifier;
    private String typeDescription;

}
