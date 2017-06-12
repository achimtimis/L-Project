package app.service.question;

import app.domain.answers.AnswerEntity;
import app.domain.questions.QuestionEntity;
import app.repository.IAnswerEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by achy_ on 6/12/2017.
 */
@Service
public class AnswerService {

    @Autowired
    private IAnswerEntityDao answerEntityDao;


    public AnswerEntity getAnswerByQuestion(QuestionEntity question) {
        return answerEntityDao.findByQuizQuestion(question);
    }
}
