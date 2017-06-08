package app.tests;

import app.domain.questions.QuestionCorrectAnswer;
import app.domain.questions.QuestionEntity;
import app.repository.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * Created by achy_ on 6/8/2017.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class QuizRepositoryTest {

    @Autowired
    private IQuizEntityDao quizEntityDao;

    @Autowired
    private IAnswerEntityDao iAnswerEntityDao;

    @Autowired
    private IQuestionEntityDao iQuestionEntityDao;

    @Autowired
    private IQuestionCorrectAnswerDao iQuestionCorrectAnswerDao;

    @Autowired
    private IQuizResponseEntiyDao iQuizResponseEntiyDao;




    @Test
    public void testQuestionCreation(){
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setId(1l);
        questionEntity.setQuestionText("Who are you?");
        questionEntity.addOption("me");
        questionEntity.addOption("myself");


        iQuestionEntityDao.save(questionEntity);
        QuestionEntity q = iQuestionEntityDao.findOne(1l);


        QuestionCorrectAnswer qca = new QuestionCorrectAnswer();
        qca.setQuestion(q);
        qca.setValidAnswers(Arrays.asList(1));
        iQuestionCorrectAnswerDao.save(qca);

        Assert.assertTrue(iQuestionCorrectAnswerDao.findAll().size() >= 1);
    }
}
