package br.com.souza.twitterclone.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {

    private String host;
    private int port;
    private String pass;

}

