package models.questions;

import lombok.Data;

import java.util.List;

/**
 * Created by achy_ on 6/10/2017.
 */
@Data
public class Question {
    private String questionText;

    private List<QuestionOptions> questionOptions;

    private List<Integer> chosenAnswer;

    private String inputField;

    private boolean isInput;
}
