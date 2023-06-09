package dc.typeface.activityapiservice.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import dc.typeface.common.models.Activity;
import dc.typeface.common.repositories.ActivityRepository;

@Service
@EnableCaching
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Cacheable(key = "#activityId", value = "Activity", cacheManager = "activity-cache-manager")
    public Optional<Activity> getActivityByid(String activityId) {
        return activityRepository.findById(activityId);
    }

}
