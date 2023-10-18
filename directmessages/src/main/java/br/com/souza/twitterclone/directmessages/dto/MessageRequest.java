package br.com.souza.twitterclone.directmessages.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MessageRequest {

    @JsonAlias("textMessage")
    private String textMessage;
    @JsonAlias("type")
    private String type;
    @JsonAlias("messageIdentifier")
    private String messageIdentifier;

}
