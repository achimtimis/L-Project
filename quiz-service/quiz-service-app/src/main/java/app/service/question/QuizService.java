package app.service.question;

import app.domain.answers.QuizResponseEntity;
import app.repository.IQuizEntityDao;
import app.repository.IQuizResponseEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by achy_ on 6/12/2017.
 */
@Service
public class QuizService {

    @Autowired
    private IQuizEntityDao quizEntityDao;

    @Autowired
    private IQuizResponseEntityDao quizResponseEntityDao;


    public QuizResponseEntity getQuizResponseEntityById(Long id){
        return quizResponseEntityDao.findOne(id);
    }
}
