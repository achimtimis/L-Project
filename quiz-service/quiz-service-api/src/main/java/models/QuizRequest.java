package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.questions.Question;
import models.utils.QuizTypeEnum;
import models.utils.TopicEnum;

import java.util.List;

/**
 * Created by achy_ on 6/10/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizRequest {

    // the quiz that will be given to the student

    private Long quiz_id;
    private List<Question> questions;

    private TopicEnum topic;

    private QuizTypeEnum quizType;

    public boolean isTimed;

    private int timer;
}
