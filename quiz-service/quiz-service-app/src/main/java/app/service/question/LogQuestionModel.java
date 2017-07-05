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

    private String quizName;
    private String quizType;
    private String questionText;
    private double questionScore;
    private boolean isAnsweredCorrectly;
    private String student;
    private String teacher;
}
