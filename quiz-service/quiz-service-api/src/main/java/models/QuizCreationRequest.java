package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import models.questions.QuestionCreatedWithAnswer;
import models.utils.QuizTypeEnum;
import models.utils.TopicEnum;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by achy_ on 6/10/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizCreationRequest {
    // includes the list of questionCreatedWithAnswers with their correct answers

    private String name;

    private List<QuestionCreatedWithAnswer> questionCreatedWithAnswers;

    private TopicEnum topic;

    private QuizTypeEnum quizType;

    public boolean isTimed;

    private int timer;

    private double minScoreToPass;

    private String creatorId;
}
