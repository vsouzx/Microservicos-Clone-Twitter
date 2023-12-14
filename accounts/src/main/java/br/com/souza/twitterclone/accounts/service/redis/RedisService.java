package br.com.souza.twitterclone.accounts.service.redis;

import br.com.souza.twitterclone.accounts.dto.auth.TokenResponse;
import br.com.souza.twitterclone.accounts.handler.exceptions.ApiAuthorizationException;
import java.util.concurrent.TimeUnit;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Service("redisService")
public class RedisService {

    private final ObjectMapper mapper;
    private final RedisTemplate<String, Object> template;

    public RedisService(ObjectMapper mapper,
                        RedisTemplate<String, Object> template) {
        this.mapper = mapper;
        this.template = template;
    }

    public void removeKey(final String key) {
        template.delete(key);
    }

    public synchronized Object getValue(final String key, Class clazz) {
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        Object obj = template.opsForValue().get(key);
        return mapper.convertValue(obj, clazz);
    }

    public void setValue(final String key, final Object value, TimeUnit unit, long timeout, boolean marshal) {
        if (marshal) {
            template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
            template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        } else {
            template.setHashValueSerializer(new StringRedisSerializer());
            template.setValueSerializer(new StringRedisSerializer());
        }
        template.opsForValue().set(key, value);
        // set a expire for a message
        template.expire(key, timeout, unit);
    }

    public void isValidUser(String userIdentifier) throws Exception {
        if(getValue("AUTH_" + userIdentifier, TokenResponse.class) == null){
            throw new ApiAuthorizationException();
        }
    }
}
