package app.repository;

import app.domain.answers.QuizResponseEntity;
import app.domain.questions.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by achy_ on 6/8/2017.
 */
@Repository
public interface IQuizResponseEntityDao extends JpaRepository<QuizResponseEntity, Long> {
    List<QuizResponseEntity> findByQuiz(QuizEntity quizEntity);

    QuizResponseEntity findByQuizAndUserId(QuizEntity quizResponseEntity, String userId);

    List<QuizResponseEntity> findByUserId(String student_id);
}
