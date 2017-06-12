package app.repository;

import app.domain.answers.AnswerEntity;
import app.domain.questions.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by achy_ on 6/8/2017.
 */
@Repository
public interface IAnswerEntityDao extends JpaRepository<AnswerEntity, Long>{
    AnswerEntity findByQuizQuestion(QuestionEntity questionEntity);
}
