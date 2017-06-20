package app.service.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by achy_ on 6/11/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogQuestionModel {

    private String quiz_name;
    private String quiz_type;
    private String questionText;
    private double qustionScore;
    private boolean isAnsweredCorrectly;
    private String userId;
}
