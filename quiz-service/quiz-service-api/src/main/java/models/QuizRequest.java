package models;

import models.questions.Question;
import models.utils.QuizTypeEnum;
import models.utils.TopicEnum;

import java.util.List;

/**
 * Created by achy_ on 6/10/2017.
 */
public class QuizRequest {

    // the quiz that will be given to the student

    private List<Question> questions;

    private TopicEnum topic;

    private QuizTypeEnum quizType;

    public boolean isTimed;

    private int timer;
}
