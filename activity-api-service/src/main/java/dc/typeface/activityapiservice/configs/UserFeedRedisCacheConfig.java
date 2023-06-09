package dc.typeface.activityapiservice.configs;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class UserFeedRedisCacheConfig {

    @Value("${user-feed-cache.host:localhost}")
    private String cacheHost;

    @Value("${user-feed-cache.port:6379}")
    private int cachePort;

    public JedisConnectionFactory redisConnectionFactor() {
        log.info("Picking the instance {}:{} as user-feed-cache", cacheHost, cachePort);
        var cf = new JedisConnectionFactory(new RedisStandaloneConfiguration(cacheHost, cachePort));
        cf.afterPropertiesSet();
        return cf;
    }

    @Bean(name = "user-feed-redis-template")
    public RedisTemplate<String, String> userFeedCacheTemplate() {
        RedisTemplate<String, String> rt = new RedisTemplate<>();
        rt.setConnectionFactory(redisConnectionFactor());
        rt.setDefaultSerializer(new StringRedisSerializer(StandardCharsets.UTF_8));
        return rt;
    }

}
