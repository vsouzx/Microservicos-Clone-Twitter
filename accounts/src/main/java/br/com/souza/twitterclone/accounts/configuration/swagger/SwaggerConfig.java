package br.com.souza.twitterclone.accounts.configuration.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value(value = "${swagger.server.url}")
    private String apiUrl;

    @Bean
    public OpenAPI myOpenAPI() {

        Contact contact = new Contact();

        contact.setEmail("vtsoliveira2001@gmail.com.br");
        contact.setName("Vitor Souza Oliveira");
        contact.setUrl("https://vsportfolio.com.br");

        Info info = new Info()
                .title("Twitter Clone Account Management Service")
                .version("1.0")
                .description("Microservice that manage users account.")
                .contact(contact);

        Server server = new Server();
        server.setUrl(apiUrl);

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(info)
                .servers(List.of(server))
                .components(new Components().addSecuritySchemes("Token de autorização", securityScheme))
                .addSecurityItem(new SecurityRequirement().addList("Token de autorização"));
    }
}
