package umc.demoday.whatisthis.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPooled;

@Configuration
public class RedisConfig {

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // key를 문자열로 직렬화
        redisTemplate.setValueSerializer(new StringRedisSerializer()); // value를 문자열로 직렬화
        return redisTemplate;
    }


    @Bean
    public JedisPooled jedisPooled() {
        return new JedisPooled(System.getenv("REDIS_HOST"), Integer.parseInt(System.getenv("REDIS_HOST")));
    }
}
