package br.com.souza.twitterclone.directmessages.dto;

import br.com.souza.twitterclone.directmessages.client.IAccountsClient;
import br.com.souza.twitterclone.directmessages.client.IFeedClient;
import br.com.souza.twitterclone.directmessages.database.model.ChatMessages;
import br.com.souza.twitterclone.directmessages.database.model.ChatMessagesReactions;
import br.com.souza.twitterclone.directmessages.database.repository.IChatMessagesReactionsRepository;
import br.com.souza.twitterclone.directmessages.dto.client.TimelineTweetResponse;
import br.com.souza.twitterclone.directmessages.dto.client.UserDetailsByIdentifierResponse;
import java.time.LocalDateTime;
import java.util.List;
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
    private List<ChatMessagesReactions> reactions;
    private Boolean seen;
    private Boolean isMine;
    private String type;

    public ChatsMessageResponse(ChatMessages message, IFeedClient feedClient, IAccountsClient iAccountClient, String chatMessageSessionUserIdentifier, String sessionUserIdentifier, String type){
        try{
            this.identifier = message.getIdentifier();
            this.chatIdentifier = message.getChatIdentifier();
            this.user = iAccountClient.getUserInfosByIdentifier(message.getUserIdentifier(), sessionUserIdentifier);
            this.text = message.getText();
            this.creationDate = message.getCreationDate();
            this.reactions = null;
            this.seen = message.getSeen();
            if(message.getTweetIdentifier() != null){
                this.tweet = feedClient.getTweetDetails(message.getTweetIdentifier(), false);
            }
            this.isMine = chatMessageSessionUserIdentifier.equals(sessionUserIdentifier);
            this.type = type;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public ChatsMessageResponse(ChatMessages message, IFeedClient feedClient, IAccountsClient iAccountClient, String sessionUserIdentifier, String type, IChatMessagesReactionsRepository iChatMessagesReactionsRepository){
        try{
            this.identifier = message.getIdentifier();
            this.chatIdentifier = message.getChatIdentifier();
            this.user = iAccountClient.getUserInfosByIdentifier(message.getUserIdentifier(), sessionUserIdentifier);
            this.text = message.getText();
            this.creationDate = message.getCreationDate();
            this.reactions = iChatMessagesReactionsRepository.findAllByIdMessageIdentifier(message.getIdentifier());
            this.seen = message.getSeen();
            if(message.getTweetIdentifier() != null){
                this.tweet = feedClient.getTweetDetails(message.getTweetIdentifier(), false);
            }
            this.isMine = sessionUserIdentifier.equals(message.getUserIdentifier());
            this.type = type;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public ChatsMessageResponse(ChatMessages message, IFeedClient feedClient, IAccountsClient iAccountClient, String sessionUserIdentifier, IChatMessagesReactionsRepository iChatMessagesReactionsRepository){
        try{
            this.identifier = message.getIdentifier();
            this.chatIdentifier = message.getChatIdentifier();
            this.user = iAccountClient.getUserInfosByIdentifier(message.getUserIdentifier(), sessionUserIdentifier);
            this.text = message.getText();
            this.creationDate = message.getCreationDate();
            this.reactions = iChatMessagesReactionsRepository.findAllByIdMessageIdentifier(message.getIdentifier());
            this.seen = message.getSeen();
            if(message.getTweetIdentifier() != null){
                this.tweet = feedClient.getTweetDetails(message.getTweetIdentifier(), false);
            }
            this.isMine = sessionUserIdentifier.equals(message.getUserIdentifier());
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
