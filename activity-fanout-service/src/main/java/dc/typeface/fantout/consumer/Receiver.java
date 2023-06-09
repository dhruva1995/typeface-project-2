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
    private RedisTemplate<String, String> userFeedTemplate;

    @Autowired
    @Qualifier("notification-redis-template")
    private RedisTemplate<String, Activity> notificationTemplate;

    @JmsListener(destination = "${queue-name}", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(ActivityMessage activityMessage) {
        Activity activity = new Activity(activityMessage.getMessage(), activityMessage.getWhen());
        String activityId = activityRepository.save(activity).getId();
        activityMessage.getUsers()
                .parallelStream()
                .forEach(user -> persistMessageForUser(activityId, user, activity));
    }

    public void persistMessageForUser(String activityId, String user, Activity activity) {
        final String userFeedKey = CacheKeyUtils.feedCacheKeyFor(user);
        userFeedTemplate.opsForList().leftPush(userFeedKey, activityId);

        final String userNoificationKey = CacheKeyUtils.notificationKeyFor(user);
        notificationTemplate.convertAndSend(userNoificationKey, activity);

        Long userFeedSize = userFeedTemplate.opsForList().size(userFeedKey);
        if (userFeedSize == null) {
            userFeedSize = 0l;
        }

        if (userFeedSize > 550) {
            userFeedTemplate.opsForList().trim(userFeedKey, 0, 499);
        }
    }

}
