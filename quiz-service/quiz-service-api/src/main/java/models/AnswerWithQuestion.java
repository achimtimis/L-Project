package models;

import lombok.Data;
import lombok.NoArgsConstructor;
import models.questions.Question;

import java.util.List;

/**
 * Created by achy_ on 6/12/2017.
 */
@Data
@NoArgsConstructor
public class AnswerWithQuestion {

    private Long id;

    private Question question;

    private String userId;

    private List<Integer> chosenOptions;

    private String inputResponse;

    private double graded_score;

    private String observation;
}
