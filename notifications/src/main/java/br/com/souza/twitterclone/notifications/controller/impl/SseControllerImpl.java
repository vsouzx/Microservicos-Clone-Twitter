package br.com.souza.twitterclone.notifications.controller.impl;

import br.com.souza.twitterclone.notifications.controller.ISseController;
import br.com.souza.twitterclone.notifications.service.notifications.ISseService;
import br.com.souza.twitterclone.notifications.service.redis.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping(value = "/v1/sse")
public class SseControllerImpl implements ISseController {

    private final ISseService iSseService;
    private final RedisService redisService;

    public SseControllerImpl(ISseService iSseService,
                             RedisService redisService) {
        this.iSseService = iSseService;
        this.redisService = redisService;
    }

    @GetMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@RequestParam("identificador") String identificador) throws Exception {
        redisService.isValidUser(identificador);
        return new ResponseEntity<>(iSseService.subscribe(identificador), HttpStatus.OK);
    }
}
