package br.com.souza.twitterclone.directmessages.configuration.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    ObjectMapper objectMapper(){
        return new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }
}
