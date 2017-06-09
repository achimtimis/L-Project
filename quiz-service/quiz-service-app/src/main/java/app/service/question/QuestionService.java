package app.service.question;

import app.domain.questions.QuestionCorrectAnswer;
import app.domain.questions.QuestionEntity;
import app.repository.IQuestionCorrectAnswerDao;
import app.repository.IQuestionEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by achy_ on 6/10/2017.
 */
@Service
public class QuestionService {

    @Autowired
    private IQuestionEntityDao questionEntityDao;

    @Autowired
    private IQuestionCorrectAnswerDao questionCorrectAnswerDao;

    public QuestionEntity saveOrUpdateQuestion(QuestionEntity q){
        return questionEntityDao.saveAndFlush(q);
    }

    public void deleteQuestion(Long id){
         questionEntityDao.delete(id);
    }

    public QuestionEntity getQuestionById(Long id){
        return questionEntityDao.getOne(id);
    }

    public QuestionCorrectAnswer addCorrectAnswerToQuestion(QuestionCorrectAnswer qca, QuestionEntity q){
        QuestionEntity questionEntity  = questionEntityDao.findOne(q.getId());
        if (questionEntity != null){
            // add correct answer
            qca.setQuestion(q);
        }
        return questionCorrectAnswerDao.saveAndFlush(qca);
    }

    public void updateCorrectAnswer(){

    }
}
