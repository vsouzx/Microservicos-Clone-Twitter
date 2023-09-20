package br.com.souza.twitterclone.notifications.service.notifications;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ISseService {

    SseEmitter subscribe(String token);
}
