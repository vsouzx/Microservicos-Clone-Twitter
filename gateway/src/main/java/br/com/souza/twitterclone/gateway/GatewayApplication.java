package br.com.souza.twitterclone.gateway;

import br.com.souza.twitterclone.gateway.security.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

	@Autowired
	private AuthenticationFilter filter;

	public static void main(String[] args) {
		new SpringApplicationBuilder(GatewayApplication.class)
				.web(WebApplicationType.REACTIVE)
				.run(args);
	}

	@Bean
	KeyResolver userKeyResolver() {
		return exchange -> Mono.just("1");
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("authentication", r -> r
						.path("/authentication/**")
						.filters(f -> f.filter(filter))
						.uri("lb://authentication"))
				.route("accounts", r -> r
						.path("/accounts/**")
						.filters(f -> f.filter(filter))
						.uri("lb://accounts"))
				.route("feed", p -> p
						.path("/feed/**")
						.filters(f -> f.filter(filter))
						.uri("lb://feed"))
				.route("notifications", p -> p
						.path("/notifications/**")
						.filters(f -> f.filter(filter))
						.uri("lb://notifications"))
				.route("directmessages", p -> p
						.path("/directmessages/**")
						.filters(f -> f.filter(filter))
						.uri("lb://directmessages"))
				.build();
	}

}
