package br.com.souza.twitterclone.directmessages.service.directmessages;

import br.com.souza.twitterclone.directmessages.dto.ChatsMessageResponse;
import br.com.souza.twitterclone.directmessages.dto.ChatsResponse;
import java.io.IOException;
import java.util.List;

public interface IDirectMessagesService {

    List<ChatsResponse> getAllChats(String sessionUserIdentifier) throws Exception ;
    List<ChatsMessageResponse> getSpecificChat(String sessionUserIdentifier, String chatIdentifier, String authorization) throws Exception;
    String initChat(String sessionUserIdentifier, String targetUserIdentifier, String auth) throws Exception;
    void cleanNoMessageChats(String sessionUserIdentifier) throws Exception;
}
