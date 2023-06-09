package dc.typeface.common.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import dc.typeface.common.models.Activity;

public interface ActivityRepository extends MongoRepository<Activity, String> {
}