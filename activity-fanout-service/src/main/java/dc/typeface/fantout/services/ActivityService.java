package dc.typeface.fantout.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dc.typeface.common.models.Activity;
import dc.typeface.common.repositories.ActivityRepository;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public String save(Activity activity) {
        var savedActivity = this.activityRepository.save(activity);
        return savedActivity.getId();
    }

}
