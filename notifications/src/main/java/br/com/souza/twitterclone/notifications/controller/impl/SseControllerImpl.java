package br.com.souza.twitterclone.notifications.controller.impl;

import br.com.souza.twitterclone.notifications.controller.ISseController;
import br.com.souza.twitterclone.notifications.service.notifications.ISseService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Validated
@RestController
@RequestMapping(value = "/v1/sse")
public class SseControllerImpl implements ISseController {

    private final ISseService iSseService;

    public SseControllerImpl(ISseService iSseService) {
        this.iSseService = iSseService;
    }

    @GetMapping(consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@RequestParam("token") String token) throws Exception {
        return iSseService.subscribe(token);
    }
}
