package models;

import lombok.Data;

import java.util.List;

/**
 * Created by achy_ on 6/10/2017.
 */
@Data
public class QuizResponseRequest {

    // the students responses for a given quiz
    private Long id;

    private String userId;

    private Long quizId;

    private List<Answer> answerList;

    private int time;

}
