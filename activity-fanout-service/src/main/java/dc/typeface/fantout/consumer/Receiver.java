package dc.typeface.fantout.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import dc.typeface.common.models.Activity;
import dc.typeface.common.repositories.ActivityRepository;
import dc.typeface.common.utils.CacheKeyUtils;
import dc.typeface.fantout.models.ActivityMessage;

@Service
public class Receiver {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    @Qualifier("user-feed-redis-template")
    private RedisTemplate<String, String> redisTemlate;

    @JmsListener(destination = "${queue-name}", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(ActivityMessage activityMessage) {
        Activity activity = new Activity(activityMessage.getMessage(), activityMessage.getWhen());
        String activityId = activityRepository.save(activity).getId();
        activityMessage.getUsers()
                .parallelStream()
                .forEach(user -> persistMessageForUser(activityId, user));
    }

    public void persistMessageForUser(String activityId, String user) {
        final String userFeedKey = CacheKeyUtils.feedCacheKeyFor(user);
        redisTemlate.opsForList().leftPush(userFeedKey, activityId);

        redisTemlate.opsForList().trim(userFeedKey, 0, 499);
    }

}
