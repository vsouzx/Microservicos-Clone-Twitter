package br.com.souza.twitterclone.directmessages.service.directmessages;

import br.com.souza.twitterclone.directmessages.dto.ChatsMessageResponse;
import br.com.souza.twitterclone.directmessages.dto.ChatsResponse;
import java.util.List;

public interface IDirectMessagesService {

    List<ChatsResponse> getAllChats(String sessionUserIdentifier) throws Exception ;
    List<ChatsMessageResponse> getSpecificChat(String sessionUserIdentifier, String chatIdentifier, Integer page, Integer size) throws Exception;
    String initChat(String sessionUserIdentifier, String targetUserIdentifier) throws Exception;
    void cleanNoMessageChats(String sessionUserIdentifier) throws Exception;
}
