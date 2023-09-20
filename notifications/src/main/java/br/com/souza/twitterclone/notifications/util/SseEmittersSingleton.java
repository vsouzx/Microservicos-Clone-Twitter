package br.com.souza.twitterclone.notifications.util;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class SseEmittersSingleton {

    private Map<String, SseEmitter> emitters;
    private static SseEmittersSingleton instance;

    public static synchronized SseEmittersSingleton getInstance() {
        if (instance == null) {
            instance = new SseEmittersSingleton();
        }
        return instance;
    }

    private SseEmittersSingleton() {
        emitters = new HashMap<>();
    }

    public void put(String identificador, SseEmitter sseEmitter) {
        emitters.put(identificador, sseEmitter);
    }

    public SseEmitter get(String identificador) {
        return emitters.get(identificador);
    }

    public void remove(String identificador) {
        emitters.remove(identificador);
    }

}
