package dc.typeface.common.models;

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

    private static final long serialVersionUID = 1686275090437L;

    @Id
    @Field(targetType = FieldType.OBJECT_ID)
    private String id;

    private String message;

    private long when;

    public Activity(final String message, final long when) {
        this.message = message;
        this.when = when;
    }

}