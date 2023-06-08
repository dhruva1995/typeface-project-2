package dc.tyoeface.fantout.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityMessage {

    private List<String> users;

    private String message;

    private long when;

}
