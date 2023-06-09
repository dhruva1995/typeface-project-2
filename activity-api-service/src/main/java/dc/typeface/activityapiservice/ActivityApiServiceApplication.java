package dc.typeface.activityapiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "dc.typeface.common.repositories")
public class ActivityApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActivityApiServiceApplication.class, args);

	}

}
