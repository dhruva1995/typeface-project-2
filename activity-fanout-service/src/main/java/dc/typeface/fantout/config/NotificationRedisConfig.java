package dc.typeface.fantout.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import dc.typeface.common.models.Activity;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class NotificationRedisConfig {

    @Value("${notification-cache.host:localhost}")
    private String notificationCacheHost;

    @Value("${notification-cache.port:6381}")
    private int notificationCachePort;

    public JedisConnectionFactory redisConnectionFactor() {
        log.info("Picking the instance {}:{} as notification-cache", notificationCacheHost, notificationCachePort);
        var cf = new JedisConnectionFactory(
                new RedisStandaloneConfiguration(notificationCacheHost, notificationCachePort));
        cf.afterPropertiesSet();
        return cf;
    }

    @Bean(name = "notification-redis-template")
    public RedisTemplate<String, Activity> notificationRedisTemplate() {
        RedisTemplate<String, Activity> rt = new RedisTemplate<>();
        rt.setConnectionFactory(redisConnectionFactor());
        rt.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return rt;
    }

}
