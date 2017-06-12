package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by achy_ on 6/12/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizToCorrectRequest {

    private Long id;

    private String userId;

    private Long quizId;

    private List<AnswerWithQuestion> answerList;

    private int time;
}
