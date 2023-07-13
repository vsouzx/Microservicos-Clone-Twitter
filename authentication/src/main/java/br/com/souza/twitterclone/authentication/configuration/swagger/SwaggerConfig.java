package br.com.souza.twitterclone.authentication.configuration.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI() {

        Contact contact = new Contact();

        contact.setEmail("vtsoliveira2001@gmail.com.br");
        contact.setName("Vitor Souza Oliveira");
        contact.setUrl("https://vsportfolio.com.br");

        Info info = new Info()
                .title("Twitter Clone Authentication")
                .version("1.0")
                .description("Microservice that authenticate users and save their tokens in Redis Database.")
                .contact(contact);

        return new OpenAPI().info(info);
    }
}
