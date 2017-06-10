package app.repository;

import app.domain.questions.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by achy_ on 6/8/2017.
 */
@Repository
public interface IQuizEntityDao extends JpaRepository<QuizEntity,Long>{
}
