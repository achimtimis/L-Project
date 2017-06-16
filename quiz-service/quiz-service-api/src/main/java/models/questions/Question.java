package models.questions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by achy_ on 6/10/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    private Long id;

    private Long questionId;

    private String questionText;

    private List<QuestionOptions> questionOptions;

    private List<Integer> chosenAnswer;

    private String inputField;

    private boolean isInput;

    private double score;
}
