package dc.typeface.activityapiservice.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import dc.typeface.common.models.Activity;
import dc.typeface.common.utils.CacheKeyUtils;

@Service
public class UserFeedService {

    @Autowired
    @Qualifier("user-feed-redis-template")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ActivityService activityService;

    public List<Activity> fetchFeed(String user) {
        final String userFeedKey = CacheKeyUtils.feedCacheKeyFor(user);
        List<String> activityIds = redisTemplate.opsForList().range(userFeedKey, 0, -1);
        return activityIds != null ? activityIds.stream()
                .parallel()
                .map(activityService::getActivityByid)
                .filter(Optional<Activity>::isPresent)
                .map(val -> val.get())
                .collect(Collectors.toList()) : Collections.emptyList();
    }

}
