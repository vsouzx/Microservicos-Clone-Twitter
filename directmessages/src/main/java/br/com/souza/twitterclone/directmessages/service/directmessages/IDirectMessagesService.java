package br.com.souza.twitterclone.directmessages.service.directmessages;

import br.com.souza.twitterclone.directmessages.dto.ChatsMessageResponse;
import br.com.souza.twitterclone.directmessages.dto.ChatsResponse;
import java.util.List;

public interface IDirectMessagesService {

    List<ChatsResponse> getAllChats(String sessionUserIdentifier);
    List<ChatsMessageResponse> getSpecificChat(String sessionUserIdentifier, String chatIdentifier, String authorization);
}
