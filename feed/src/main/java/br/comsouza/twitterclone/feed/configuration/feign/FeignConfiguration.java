package br.comsouza.twitterclone.feed.configuration.feign;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
            template.header("LOGGED_USER_IDENTIFIER", request.getHeader("LOGGED_USER_IDENTIFIER"));
        };
    }
}