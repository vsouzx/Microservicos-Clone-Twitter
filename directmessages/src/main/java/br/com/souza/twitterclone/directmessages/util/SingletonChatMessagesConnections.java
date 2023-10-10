package br.com.souza.twitterclone.directmessages.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.WebSocketSession;

public class SingletonChatMessagesConnections {

    private Map<String, Set<WebSocketSession>> chatMessagesConnections;
    private static SingletonChatMessagesConnections instance;

    public static synchronized SingletonChatMessagesConnections getInstance() {
        if (instance == null) {
            instance = new SingletonChatMessagesConnections();
        }
        return instance;
    }

    private SingletonChatMessagesConnections() {
        chatMessagesConnections = new ConcurrentHashMap<>();
    }

    public void put(String identificador, WebSocketSession webSocketSession) {
        chatMessagesConnections.computeIfAbsent(identificador, k -> new HashSet<>());
        chatMessagesConnections.get(identificador).add(webSocketSession);
    }

    public Set<WebSocketSession> get(String identificador) {
        return chatMessagesConnections.get(identificador);
    }

    public void remove(String identificador, WebSocketSession session) {
        chatMessagesConnections.get(identificador).remove(session);
    }
}
