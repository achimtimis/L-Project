package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.questions.Question;

/**
 * Created by achy_ on 6/12/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnsweredQuestion {

    private Question question;
    private Answer answer;
    private double score;
    private double maxScore;
    private String observation;

}
