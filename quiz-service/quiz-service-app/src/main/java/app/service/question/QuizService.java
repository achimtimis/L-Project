package app.service.question;

import app.domain.answers.QuizResponseEntity;
import app.domain.questions.QuizEntity;
import app.domain.results.ResultEntity;
import app.repository.IQuizEntityDao;
import app.repository.IQuizResponseEntityDao;
import models.QuizRequest;
import models.QuizStudentResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by achy_ on 6/12/2017.
 */
@Service
public class QuizService {

    @Autowired
    private IQuizEntityDao quizEntityDao;

    @Autowired
    private IQuizResponseEntityDao quizResponseEntityDao;


    public QuizResponseEntity getQuizResponseEntityById(Long id) {
        return quizResponseEntityDao.findOne(id);
    }

    public QuizResponseEntity saveOrUpdateQuizResponse(QuizResponseEntity quizResponseEntity) {
        return quizResponseEntityDao.saveAndFlush(quizResponseEntity);
    }

    public QuizEntity save(QuizEntity quizEntity) {
        return quizEntityDao.save(quizEntity);
    }

    public QuizEntity findOne(Long quizId) {
        return quizEntityDao.findOne(quizId);
    }

    public QuizResponseEntity save(QuizResponseEntity quizResponseEntity) {
        return quizResponseEntityDao.save(quizResponseEntity);
    }

    public QuizResponseEntity getQuizResponseEntityByStudentIdAndQuizId(QuizEntity qe, String userId) {
        return quizResponseEntityDao.findByQuizAndUserId(qe, userId);
    }

    public List<QuizEntity> getAllQuizesOfAStudent(String studentid) {
        List<QuizEntity> result = new ArrayList<>();
        List<QuizEntity> quizEntities = quizEntityDao.findAll();
        for (QuizEntity qe : quizEntities) {
            List<QuizResponseEntity> quizResponseEntities = quizResponseEntityDao.findByQuiz(qe);
            if (quizResponseEntities.size() ==0){
                result.add(qe);
            }
            for (QuizResponseEntity qre : quizResponseEntities) {
                if (!qre.getUserId().equals(studentid)) {
                    result.add(qe);
                }
            }

        }
        return result;
    }


}
