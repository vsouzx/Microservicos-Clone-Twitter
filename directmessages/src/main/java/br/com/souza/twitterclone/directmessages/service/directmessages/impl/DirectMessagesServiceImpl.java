package br.com.souza.twitterclone.directmessages.service.directmessages.impl;

import br.com.souza.twitterclone.directmessages.client.IAccountsClient;
import br.com.souza.twitterclone.directmessages.client.IFeedClient;
import br.com.souza.twitterclone.directmessages.database.model.DmChats;
import br.com.souza.twitterclone.directmessages.database.repository.IChatMessagesRepository;
import br.com.souza.twitterclone.directmessages.database.repository.IDmChatsRepository;
import br.com.souza.twitterclone.directmessages.database.repository.impl.LoadDirectMessagesRepositoryImpl;
import br.com.souza.twitterclone.directmessages.dto.ChatsMessageResponse;
import br.com.souza.twitterclone.directmessages.dto.ChatsResponse;
import br.com.souza.twitterclone.directmessages.service.directmessages.IDirectMessagesService;
import br.com.souza.twitterclone.directmessages.util.SingletonDmChatsConnections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class DirectMessagesServiceImpl implements IDirectMessagesService {

    private final LoadDirectMessagesRepositoryImpl loadDirectMessagesRepository;
    private final IChatMessagesRepository chatMessagesRepository;
    private final IDmChatsRepository iDmChatsRepository;
    private final IFeedClient iFeedClient;
    private final IAccountsClient iAccountsClient;
    private final SingletonDmChatsConnections singletonDmChatsConnections;

    public DirectMessagesServiceImpl(LoadDirectMessagesRepositoryImpl loadDirectMessagesRepository,
                                     IChatMessagesRepository chatMessagesRepository,
                                     IDmChatsRepository iDmChatsRepository,
                                     IFeedClient iFeedClient,
                                     IAccountsClient iAccountsClient) {
        this.loadDirectMessagesRepository = loadDirectMessagesRepository;
        this.chatMessagesRepository = chatMessagesRepository;
        this.iDmChatsRepository = iDmChatsRepository;
        this.iFeedClient = iFeedClient;
        this.iAccountsClient = iAccountsClient;
        this.singletonDmChatsConnections = SingletonDmChatsConnections.getInstance();
    }

    public List<ChatsResponse> getAllChats(String sessionUserIdentifier){
        return loadDirectMessagesRepository.getAllChats(sessionUserIdentifier);
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

    @Override
    public String initChat(String sessionUserIdentifier, String targetUserIdentifier, String auth) throws Exception {
        Optional<DmChats> possibleChat = iDmChatsRepository.findByUserIdentifier1AndUserIdentifier2(sessionUserIdentifier, targetUserIdentifier);
        if(possibleChat.isEmpty()){
            possibleChat = iDmChatsRepository.findByUserIdentifier1AndUserIdentifier2(targetUserIdentifier, sessionUserIdentifier);
        }
        if(possibleChat.isEmpty()){
            possibleChat = Optional.of(iDmChatsRepository.save(DmChats.builder()
                    .identifier(UUID.randomUUID().toString())
                    .userIdentifier1(sessionUserIdentifier)
                    .userIdentifier2(targetUserIdentifier)
                    .build()));
        }
        return possibleChat.get().getIdentifier();
    }

    @Override
    public void cleanNoMessageChats(String sessionUserIdentifier){
        List<ChatsResponse> chatsWithNoMessages = loadDirectMessagesRepository.getAllChats(sessionUserIdentifier).stream()
                .filter(c -> c.getLastMessageText() == null)
                .toList();

        for(ChatsResponse chat : chatsWithNoMessages){
            iDmChatsRepository.deleteById(chat.getChatIdentifier());
        }
    }
}
