package br.com.souza.twitterclone.directmessages.service.redis;

import br.com.souza.twitterclone.directmessages.dto.auth.TokenResponse;
import br.com.souza.twitterclone.directmessages.handler.exceptions.ApiAuthorizationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

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

    public synchronized Object getValue(final String key, Class clazz) {
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        Object obj = template.opsForValue().get(key);
        return mapper.convertValue(obj, clazz);
    }

    public void isValidUser(String userIdentifier) throws Exception {
        if(getValue("AUTH_" + userIdentifier, TokenResponse.class) == null){
            throw new ApiAuthorizationException();
        }
    }


}
