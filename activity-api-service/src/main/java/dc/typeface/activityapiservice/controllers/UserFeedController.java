package dc.typeface.activityapiservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dc.typeface.activityapiservice.services.UserFeedService;
import dc.typeface.common.models.Activity;

@RestController
@RequestMapping("/api/v1/feed")
public class UserFeedController {

    @Autowired
    private UserFeedService userFeedService;

    @GetMapping("/{user}")
    public List<Activity> getUserFeed(@PathVariable("user") String user) {
        return userFeedService.fetchFeed(user);
    }

}
