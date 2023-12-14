package br.com.souza.twitterclone.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private String jwtKey;

}

