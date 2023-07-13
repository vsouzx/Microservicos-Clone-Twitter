package br.com.souza.twitterclone.gateway.filter;

import br.com.souza.twitterclone.gateway.service.ErrorReturnTransferImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingGlobalRegister implements GlobalFilter, Ordered {

    private static final Set<String> LOGGABLE_CONTENT_TYPES = new HashSet<>(Arrays.asList(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.TEXT_PLAIN_VALUE,
            MediaType.TEXT_XML_VALUE
    ));
    private static final Set<String> STATUS_CODE_SUCCESS = new HashSet<>(Arrays.asList(
            String.valueOf(HttpStatus.OK.value()), String.valueOf(HttpStatus.CREATED.value())
    ));

    private final ObjectMapper objectMapper;
    private final ErrorReturnTransferImpl errorReturnTransfer;


    public LoggingGlobalRegister(ErrorReturnTransferImpl errorReturnTransfer,
                                 ObjectMapper objectMapper) {
        this.errorReturnTransfer = errorReturnTransfer;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var decoratedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (LOGGABLE_CONTENT_TYPES.contains(String.valueOf(getHeaders().getContentType()).toLowerCase())) {
                    return join(body).flatMap(db -> {
                        log.debug("[Gateway] Filter Pos Register");
                        String responseData = StandardCharsets.UTF_8.decode(db.asByteBuffer()).toString();
                        final String statusCode = String.valueOf(exchange.getResponse().getStatusCode().value());
                        if (STATUS_CODE_SUCCESS.contains(statusCode)) {
                            return getDelegate().writeWith(Mono.just(db));
                        } else {
                            byte[] bytes = errorReturnTransfer.process(objectMapper, responseData);
                            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                            return getDelegate().writeWith(Mono.just(buffer));
                        }
                    });
                } else {
                    return getDelegate().writeWith(body);
                }
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -2;
    }

    private Mono<? extends DataBuffer> join(Publisher<? extends DataBuffer> dataBuffers) {
        Assert.notNull(dataBuffers, "'dataBuffers' must not be null");
        return Flux.from(dataBuffers)
                .collectList()
                .filter((list) -> !list.isEmpty())
                .map((list) -> list.get(0).factory().join(list))
                .doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);
    }
}

