package br.comsouza.twitterclone.feed.service.redis;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Service("redisService")
public class RedisService {

    private final RedisTemplate<String, Object> template;

    public RedisService(RedisTemplate<String, Object> template) {
        this.template = template;
    }

    public synchronized Object getValue(final String key) {

        template.setHashValueSerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template.opsForValue().get(key);
    }

}
