package models.questions;

import lombok.Data;

import java.util.List;

/**
 * Created by achy_ on 6/10/2017.
 */
@Data
public class QuestionCreatedWithAnswer {

    private String questionText;

    private List<QuestionOptions> questionOptions;

    private List<Integer> correctAnswers;

    private boolean isInput;

}
