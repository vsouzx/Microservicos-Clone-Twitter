package br.com.souza.twitterclone.directmessages.service.directmessages.impl;

import br.com.souza.twitterclone.directmessages.client.IAccountsClient;
import br.com.souza.twitterclone.directmessages.client.IFeedClient;
import br.com.souza.twitterclone.directmessages.database.model.ChatMessages;
import br.com.souza.twitterclone.directmessages.database.repository.IChatMessagesRepository;
import br.com.souza.twitterclone.directmessages.database.repository.impl.LoadDirectMessagesRepositoryImpl;
import br.com.souza.twitterclone.directmessages.dto.ChatsMessageResponse;
import br.com.souza.twitterclone.directmessages.dto.ChatsResponse;
import br.com.souza.twitterclone.directmessages.service.directmessages.IDirectMessagesService;
import br.com.souza.twitterclone.directmessages.util.SingletonDmChatsConnections;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class DirectMessagesServiceImpl implements IDirectMessagesService {

    private final LoadDirectMessagesRepositoryImpl loadDirectMessagesRepository;
    private final IChatMessagesRepository chatMessagesRepository;
    private final IFeedClient iFeedClient;
    private final IAccountsClient iAccountsClient;
    private final SingletonDmChatsConnections singletonDmChatsConnections;

    public DirectMessagesServiceImpl(LoadDirectMessagesRepositoryImpl loadDirectMessagesRepository,
                                     IChatMessagesRepository chatMessagesRepository,
                                     IFeedClient iFeedClient,
                                     IAccountsClient iAccountsClient) {
        this.loadDirectMessagesRepository = loadDirectMessagesRepository;
        this.chatMessagesRepository = chatMessagesRepository;
        this.iFeedClient = iFeedClient;
        this.iAccountsClient = iAccountsClient;
        this.singletonDmChatsConnections = SingletonDmChatsConnections.getInstance();
    }

    public List<ChatsResponse> getAllChats(String sessionUserIdentifier){
        return loadDirectMessagesRepository.getAllChats(sessionUserIdentifier).stream()
                .sorted(Comparator.comparing(ChatsResponse::getLastMessageDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatsMessageResponse> getSpecificChat(String sessionUserIdentifier, String chatIdentifier, String authorization) throws Exception {

        chatMessagesRepository.findAllByChatIdentifier(chatIdentifier).stream()
                .filter(m -> !m.getUserIdentifier().equals(sessionUserIdentifier))
                .forEach(m -> {
                    m.setSeen(true);
                    chatMessagesRepository.save(m);
                });

        Set<WebSocketSession> sessions = singletonDmChatsConnections.get(sessionUserIdentifier);
        if(sessions != null){
            for (WebSocketSession s : sessions){
                s.sendMessage(new TextMessage("updated"));
            }
        }


        return chatMessagesRepository.findAllByChatIdentifier(chatIdentifier)
                .stream()
                .map(m -> new ChatsMessageResponse(m, iFeedClient, iAccountsClient, authorization, sessionUserIdentifier))
                .sorted(Comparator.comparing(ChatsMessageResponse::getCreationDate))
                .toList();
    }
}
