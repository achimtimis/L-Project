package models.questions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by achy_ on 6/10/2017.
 */
@Data
@AllArgsConstructor
public class QuestionCreatedWithAnswer {

    private String questionText;

    private List<QuestionOptions> questionOptions;

    private List<Integer> correctAnswers;

    private boolean isInput;

}
