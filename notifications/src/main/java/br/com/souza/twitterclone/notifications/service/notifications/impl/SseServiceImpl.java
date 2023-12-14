package br.com.souza.twitterclone.notifications.service.notifications.impl;

import br.com.souza.twitterclone.notifications.service.notifications.ISseService;
import br.com.souza.twitterclone.notifications.util.SseEmittersSingleton;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseServiceImpl implements ISseService {

    private final SseEmittersSingleton sseEmittersSingleton;
    private static final Long EXPIRATION = 600_000L;
    private static final String EVENT_NAME = "new-notification";

    public SseServiceImpl() {
        this.sseEmittersSingleton = SseEmittersSingleton.getInstance();
    }

    @Override
    public SseEmitter subscribe(String identificador) {
        SseEmitter sseEmitter = instanciarNovoEmitter(identificador);

        sseEmitter.onCompletion(() -> {
            sseEmittersSingleton.remove(identificador);
        });

        sseEmitter.onError((e) -> {
            sseEmitter.completeWithError(e);
            sseEmittersSingleton.remove(identificador);
        });

        sseEmitter.onTimeout(() -> {
            sseEmitter.complete();
            sseEmittersSingleton.remove(identificador);
        });
        return sseEmitter;
    }

    @Override
    public void notifyFrontend(String userToBeNotified, String notificationType, String username) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("type", notificationType);
        objectNode.put("username", username);

        String jsonMessage = objectMapper.writeValueAsString(objectNode);

        SseEmitter emitter = sseEmittersSingleton.get(userToBeNotified);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(EVENT_NAME).data(jsonMessage));
            } catch (Exception e) {
                emitter.completeWithError(e);
                sseEmittersSingleton.remove(userToBeNotified);
            }
        }
    }

    private SseEmitter instanciarNovoEmitter(final String identificador) {
        sseEmittersSingleton.remove(identificador);
        SseEmitter sseEmitter = new SseEmitter(EXPIRATION);
        sseEmittersSingleton.put(identificador, sseEmitter);

        try {
            sseEmitter.send(SseEmitter.event().name("OK").data("ok"));
        } catch (Exception e) {
            sseEmitter.completeWithError(e);
            sseEmittersSingleton.remove(identificador);
        }
        return sseEmitter;
    }

}
