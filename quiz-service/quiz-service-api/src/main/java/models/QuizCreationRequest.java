package models;

import lombok.Data;
import models.questions.QuestionCreatedWithAnswer;
import models.utils.QuizTypeEnum;
import models.utils.TopicEnum;

import java.util.List;

/**
 * Created by achy_ on 6/10/2017.
 */
@Data
public class QuizCreationRequest {
    // includes the list of questionCreatedWithAnswers with their correct answers


    private List<QuestionCreatedWithAnswer> questionCreatedWithAnswers;

    private TopicEnum topic;

    private QuizTypeEnum quizType;

    public boolean isTimed;

    private int timer;
}
