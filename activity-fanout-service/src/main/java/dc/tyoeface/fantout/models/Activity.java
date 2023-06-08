package dc.tyoeface.fantout.models;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "activity-feed")
public class Activity implements Serializable {

    @Id
    @Field(targetType = FieldType.OBJECT_ID)
    private String id;

    private String message;

    private long when;

    public Activity(final ActivityMessage activityMsg) {
        this.message = activityMsg.getMessage();
        this.when = activityMsg.getWhen();
    }
}
