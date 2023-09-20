package br.com.souza.twitterclone.notifications.service.notifications.impl;

import br.com.souza.twitterclone.notifications.configuration.authorization.TokenProvider;
import br.com.souza.twitterclone.notifications.service.notifications.ISseService;
import br.com.souza.twitterclone.notifications.service.redis.RedisService;
import br.com.souza.twitterclone.notifications.util.SseEmittersSingleton;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseServiceImpl implements ISseService {

    private final TokenProvider tokenProvider;
    private final RedisService redisService;
    private final SseEmittersSingleton sseEmittersSingleton;
    private static final Long EXPIRATION = 600_000L;

    public SseServiceImpl(TokenProvider tokenProvider,
                          RedisService redisService) {
        this.tokenProvider = tokenProvider;
        this.redisService = redisService;
        this.sseEmittersSingleton = SseEmittersSingleton.getInstance();
    }

    @Override
    public SseEmitter subscribe(String token) {
        if (tokenProvider.validateTokenForEmitter(token)) {
            String sessionId = tokenProvider.getIdentifierFromToken(token);
            final String identificador = (String) redisService.getValue(sessionId);

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
        return null;
    }

    private SseEmitter instanciarNovoEmitter(final String identificador) {
        SseEmitter sseEmitter = sseEmittersSingleton.get(identificador);

        if(sseEmitter == null){
            sseEmitter = new SseEmitter(EXPIRATION);
            sseEmittersSingleton.remove(identificador);
            sseEmittersSingleton.put(identificador, sseEmitter);
        }

        return sseEmitter;
    }

}
