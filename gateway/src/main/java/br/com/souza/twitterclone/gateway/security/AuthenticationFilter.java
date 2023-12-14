package br.com.souza.twitterclone.gateway.security;

import java.util.List;
import java.util.function.Predicate;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GatewayFilter, Ordered {

    private final TokenProvider jwtUtil;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String USER_HEADER = "LOGGED_USER_IDENTIFIER";

    public AuthenticationFilter(TokenProvider jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        final List<String> apiEndpoints = List.of(
                //Swagger
                "/v3/api-docs",
                "/v3/api-docs/swagger-config",
                "/swagger-ui.html",
                "/swagger-ui",

                //Authentication Service
                "/authentication/v1/connect",

                //Accounts Service
                "/accounts/v1/user/register",
                "/accounts/v1/user/search/isvaliduser",
                "/accounts/v1/user/search/isvalidusername",
                "/accounts/v1/user/search/isvalidemail",

                //Feed Service
                "/feed/v1/user/register/**",

                //Notifications Service
                "/notifications/v1/sse",

                // WebSockets liberados
                "/directmessages/ws"

        );

        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));

        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey(AUTHORIZATION_HEADER)) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            final String token = request.getHeaders().getOrEmpty(AUTHORIZATION_HEADER).get(0).replace("Bearer ", "");

            try {
                jwtUtil.isValid(token);
            } catch (Exception e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            String identifier = jwtUtil.getIdentifierFromToken(token);
            exchange.getRequest().mutate().header(USER_HEADER, identifier).build();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -2;
    }
}