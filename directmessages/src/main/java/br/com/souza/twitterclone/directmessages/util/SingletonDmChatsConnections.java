package br.com.souza.twitterclone.directmessages.util;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.WebSocketSession;

public class SingletonDmChatsConnections {

    private Map<String, Set<WebSocketSession>> dmChatsConnections;
    private static SingletonDmChatsConnections instance;

    public static synchronized SingletonDmChatsConnections getInstance() {
        if (instance == null) {
            instance = new SingletonDmChatsConnections();
        }
        return instance;
    }

    private SingletonDmChatsConnections() {
        dmChatsConnections = new ConcurrentHashMap<>();
    }

    public void put(String identificador, WebSocketSession webSocketSession) {
        dmChatsConnections.computeIfAbsent(identificador, k -> new HashSet<>());
        dmChatsConnections.get(identificador).add(webSocketSession);
    }

    public Set<WebSocketSession> get(String identificador) {
        return dmChatsConnections.get(identificador);
    }

    public void remove(String identificador, WebSocketSession session) {
        dmChatsConnections.get(identificador).remove(session);
    }
}
