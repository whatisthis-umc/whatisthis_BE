package umc.demoday.whatisthis.global.config;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPooled;

@Configuration
// ✅ 1. Redis OM Spring의 Repository 기능을 활성화하는 어노테이션 추가
@EnableRedisDocumentRepositories(basePackages = "umc.demoday.whatisthis.domain.*")
public class RedisConfig {

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // key를 문자열로 직렬화
        redisTemplate.setValueSerializer(new StringRedisSerializer()); // value를 문자열로 직렬화
        return redisTemplate;
    }
    // JedisConnectionFactory Bean 등록
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(System.getenv("REDIS_HOST"), 6379);
        return new JedisConnectionFactory(config);
    }


}
