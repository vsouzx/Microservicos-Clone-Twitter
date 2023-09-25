package br.comsouza.twitterclone.feed.dto.client;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String typeDescription;
    @NotNull
    private String userSenderIdentifier;
    @NotNull
    private String userReceiverIdentifier;

}