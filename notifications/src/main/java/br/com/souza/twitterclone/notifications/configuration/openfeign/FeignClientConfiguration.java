package br.com.souza.twitterclone.notifications.configuration.openfeign;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration{

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecode();
    }

}