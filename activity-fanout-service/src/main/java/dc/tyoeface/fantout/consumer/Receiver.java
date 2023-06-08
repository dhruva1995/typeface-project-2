package dc.tyoeface.fantout.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import dc.tyoeface.fantout.models.Activity;
import dc.tyoeface.fantout.models.ActivityMessage;
import dc.tyoeface.fantout.repositories.ActivityRepository;

@Service
public class Receiver {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemlate;

    @JmsListener(destination = "${queue-name}", containerFactory = "myFactory")
    public void receiveMessage(ActivityMessage activityMessage) {
        Activity activity = new Activity(activityMessage);
        String activityId = activityRepository.save(activity).getId();
        activityMessage.getUsers()
                .parallelStream()
                .forEach(user -> persistMessageForUser(activityId, user));
    }

    public void persistMessageForUser(String activityId, String user) {
        final String userFeedKey = "user-feed-" + user;
        redisTemlate.opsForList().leftPush(userFeedKey, activityId);
        redisTemlate.opsForList().trim(userFeedKey, 0, 199);
    }

}
