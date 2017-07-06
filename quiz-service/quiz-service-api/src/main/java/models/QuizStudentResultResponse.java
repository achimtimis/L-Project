package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
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
public class QuizStudentResultResponse {

    // GET the results of a quiz for a given student
    private Long id;
    private Long quiz_id;
    private String quiz_name;
    private List<AnsweredQuestion> answeredQuestions;
    private double score;
    private double totalScore;
    @JsonProperty(value = "isPassed")
    private boolean isPassed;
    @JsonProperty(value = "isCorrected")
    private boolean isCorrected;
    private String extraFeedback;
    private int totalDuration;
    private String student;

}
