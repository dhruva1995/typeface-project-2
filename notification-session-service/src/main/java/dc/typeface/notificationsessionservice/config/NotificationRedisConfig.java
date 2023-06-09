package dc.typeface.notificationsessionservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class NotificationRedisConfig {

    @Value("${notification-cache.host:localhost}")
    private String notificationCacheHost;

    @Value("${notification-cache.port:6381}")
    private int notificationCachePort;

    public LettuceConnectionFactory redisConnectionFactory() {
        log.info("Picking the instance {}:{} as notification-cache", notificationCacheHost, notificationCachePort);
        var cf = new LettuceConnectionFactory(
                new RedisStandaloneConfiguration(notificationCacheHost, notificationCachePort));

        cf.afterPropertiesSet();
        return cf;
    }

    @Bean
    RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        return container;
    }

}
