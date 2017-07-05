package models;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    private String quiz_name;

    private Long quizId;

    private List<AnswerWithQuestion> answerList;

    private int time;

    private double minScoreToPass;

    private double score;

    private double totalScore;

    @JsonProperty(value = "isPassed")
    private boolean isPassed;

    private String extraFeedback;
}
