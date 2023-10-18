package br.com.souza.twitterclone.directmessages.dto;

import br.com.souza.twitterclone.directmessages.client.IAccountsClient;
import br.com.souza.twitterclone.directmessages.client.IFeedClient;
import br.com.souza.twitterclone.directmessages.database.model.ChatMessages;
import br.com.souza.twitterclone.directmessages.dto.client.TimelineTweetResponse;
import br.com.souza.twitterclone.directmessages.dto.client.UserDetailsByIdentifierResponse;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChatsMessageResponse {

    private String identifier;
    private String chatIdentifier;
    private UserDetailsByIdentifierResponse user;
    private String text;
    private TimelineTweetResponse tweet;
    private LocalDateTime creationDate;
    private String emoji;
    private Boolean seen;
    private Boolean isMine;
    private String type;

    public ChatsMessageResponse(ChatMessages message, IFeedClient feedClient, IAccountsClient iAccountClient, String authorization, String sessionUserIdentifier, String type){
        try{
            this.identifier = message.getIdentifier();
            this.chatIdentifier = message.getChatIdentifier();
            this.user = iAccountClient.getUserInfosByIdentifier(message.getUserIdentifier(), authorization);
            this.text = message.getText();
            this.creationDate = message.getCreationDate();
            this.emoji = message.getEmoji();
            this.seen = message.getSeen();
            if(message.getTweetIdentifier() != null){
                this.tweet = feedClient.getTweetDetails(message.getTweetIdentifier(), authorization, false);
            }
            this.isMine = sessionUserIdentifier.equals(message.getUserIdentifier());
            this.type = type;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
