package dc.typeface.activityapiservice.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class AcivityRedisCacheConfig {
    @Value("${activity-cache.host:localhost}")
    private String cacheHost;

    @Value("${activity-cache.port:6380}")
    private int cachePort;

    private JedisConnectionFactory redisConnectionFactory() {
        log.info("Picking the instance {}:{} as activity-cache", cacheHost, cachePort);
        var cf = new JedisConnectionFactory(new RedisStandaloneConfiguration(cacheHost, cachePort));
        cf.afterPropertiesSet();
        return cf;
    }

    private RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean("activity-cache-manager")
    public RedisCacheManager cacheManager() {
        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(cacheConfiguration())
                .transactionAware()
                .build();

    }

}
