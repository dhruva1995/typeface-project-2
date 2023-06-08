package dc.tyoeface.fantout.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import dc.tyoeface.fantout.models.Activity;

public interface ActivityRepository extends MongoRepository<Activity, String> {
}