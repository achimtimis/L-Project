package app.tests;

import app.domain.answers.AnswerEntity;
import app.domain.answers.QuizResponseEntity;
import app.domain.questions.QuestionCorrectAnswer;
import app.domain.questions.QuestionEntity;
import app.domain.questions.QuizEntity;

import app.repository.*;
import models.utils.QuizTypeEnum;
import models.utils.TopicEnum;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by achy_ on 6/8/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
//@Rollback(value = false)
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

    private Long goodQuizId;


    @Test
//    @Ignore
    public void testQuestionCreation(){
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setQuestionText("Who are you?");
        questionEntity.addOption("me");
        questionEntity.addOption("myself");

        QuestionCorrectAnswer qca = new QuestionCorrectAnswer();
//        qca.setQuestion(q);
        qca.setValidAnswers(Arrays.asList(1));
        questionEntity.setQuestionCorrectAnswer(qca);
        QuestionEntity q = iQuestionEntityDao.save(questionEntity);

//        QuestionCorrectAnswer qca = new QuestionCorrectAnswer();
//        qca.setQuestion(q);
//        qca.setValidAnswers(Arrays.asList(1));
//        QuestionCorrectAnswer questionCorrectAnswer = iQuestionCorrectAnswerDao.saveAndFlush(qca);

//        Assert.assertTrue(iQuestionCorrectAnswerDao.findAll().contains(questionCorrectAnswer));

        QuizEntity quizEntity = new QuizEntity();
        quizEntity.setQuiz_questions(Arrays.asList(q));
        quizEntity.setQuizType(QuizTypeEnum.SINGLE_ANSWER);
        quizEntity.setTopic(TopicEnum.CS);
        QuizEntity saved = quizEntityDao.saveAndFlush(quizEntity);

        Assert.assertTrue(saved.getQuiz_questions().size() > 0);

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setCiamUserId("user1");
        answerEntity.setQuiz_question(q);
        answerEntity.setOption_responses(Arrays.asList(1));
        AnswerEntity savedAnswer = iAnswerEntityDao.saveAndFlush(answerEntity);

        Assert.assertTrue(iAnswerEntityDao.findAll().contains(savedAnswer));




    }
    @Test
//    @Ignore
    public void testAnswer(){
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setQuestionText("Who are you?");
        questionEntity.addOption("me");
        questionEntity.addOption("myself");
        QuestionEntity questionEntity2 = new QuestionEntity();
        questionEntity2.setQuestionText("Who aare you?");
        questionEntity2.addOption("me");
        questionEntity2.addOption("myself");

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setCiamUserId("user1");
        answerEntity.setQuiz_question(questionEntity);
        answerEntity.setOption_responses(Arrays.asList(1));


        AnswerEntity answerEntity2 = new AnswerEntity();
        answerEntity.setCiamUserId("user2");
        answerEntity.setQuiz_question(questionEntity);
        answerEntity.setOption_responses(Arrays.asList(1));


        QuizEntity quizEntity = new QuizEntity();
        quizEntity.setQuiz_questions(Arrays.asList(questionEntity, questionEntity2));
        quizEntity.setQuizType(QuizTypeEnum.SINGLE_ANSWER);
        quizEntity.setTopic(TopicEnum.CS);

        QuizResponseEntity quizResponseEntity = new QuizResponseEntity();
//        answerEntity.setQuizResponse(quizResponseEntity);
//        answerEntity2.setQuizResponse(quizResponseEntity);
        quizResponseEntity.setAnswers(Arrays.asList(answerEntity, answerEntity2));
        quizResponseEntity.setQuiz(quizEntity);
        quizResponseEntity.setUserId("user1");
        QuizResponseEntity saved = iQuizResponseEntiyDao.saveAndFlush(quizResponseEntity);

        Assert.assertTrue(saved.getAnswers().size() ==2);
        Assert.assertTrue(saved.getQuiz().getQuiz_questions().size() == 2);
        List<QuestionEntity> list = new ArrayList<QuestionEntity>(saved.getQuiz().getQuiz_questions());
        Assert.assertTrue(list.get(0).getQuestionText().contains("you"));
        Assert.assertTrue(saved.getAnswers().get(0).getQuizResponse().getId() == saved.getId());
        goodQuizId = saved.getId();

    }

    @Test
//    @Ignore
    public void testDeletes(){
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setQuestionText("Who are you q1?");
        questionEntity.addOption("me");
        questionEntity.addOption("myself");

        QuestionEntity questionEntity2 = new QuestionEntity();
        questionEntity2.setQuestionText("Who aare you q2?");
        questionEntity2.addOption("me");
        questionEntity2.addOption("myself");

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setCiamUserId("user1");
        answerEntity.setQuiz_question(questionEntity);
        answerEntity.setOption_responses(Arrays.asList(1));


        AnswerEntity answerEntity2 = new AnswerEntity();
        answerEntity2.setCiamUserId("user2");
        answerEntity2.setQuiz_question(questionEntity);
        answerEntity2.setOption_responses(Arrays.asList(1));


        QuizEntity quizEntity = new QuizEntity();
        quizEntity.setQuiz_questions(Arrays.asList(questionEntity, questionEntity2));
        quizEntity.setQuizType(QuizTypeEnum.SINGLE_ANSWER);
        quizEntity.setTopic(TopicEnum.CS);

        QuizResponseEntity quizResponseEntity = new QuizResponseEntity();
//        answerEntity.setQuizResponse(quizResponseEntity);
//        answerEntity2.setQuizResponse(quizResponseEntity);
        quizResponseEntity.setAnswers(Arrays.asList(answerEntity, answerEntity2));
        quizResponseEntity.setQuiz(quizEntity);
        quizResponseEntity.setUserId("user1");
        QuizResponseEntity saved = iQuizResponseEntiyDao.saveAndFlush(quizResponseEntity);
        QuizResponseEntity qre = iQuizResponseEntiyDao.findOne(saved.getId());
        int beforeDeletion = qre.getAnswers().size();
        AnswerEntity ans1 = new ArrayList<AnswerEntity>(qre.getAnswers()).get(0);
        List<AnswerEntity> answerEntities = qre.getAnswers();
        answerEntities.remove(0);
        qre.setAnswers(answerEntities);
        iQuizResponseEntiyDao.saveAndFlush(qre);
        iAnswerEntityDao.delete(ans1.getId());
        QuizResponseEntity qre2 = iQuizResponseEntiyDao.findOne(qre.getId());
        Assert.assertTrue(beforeDeletion == (qre2.getAnswers().size() - 1));
    }

    @Test
    public void testAddQuestionNoQuiz(){

        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setQuestionText("Who are you?");
        questionEntity.addOption("me");
        questionEntity.addOption("myself");


        QuestionEntity q = iQuestionEntityDao.save(questionEntity);
        Assert.assertTrue(q.getQuestionText().contains("are"));
        Assert.assertTrue(q.getOptions().size() == 2);
    }
    @Test
    public void testAddQuestionQuiz(){
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setQuestionText("Who are quiz working?");
        questionEntity.addOption("me");
        questionEntity.addOption("myself");


       QuizEntity qe = new QuizEntity();
       qe.setQuizType(QuizTypeEnum.SINGLE_ANSWER);
       qe.setQuiz_questions(Arrays.asList(questionEntity));
       qe.setTimer(12);
       QuizEntity q2 = quizEntityDao.saveAndFlush(qe);
        Assert.assertTrue(q2.getQuiz_questions().contains(questionEntity));

        iQuestionEntityDao.delete(q2.getQuiz_questions().get(0).getId());
        Assert.assertTrue(iQuestionEntityDao.findAll().size() ==0);
    }

    @Test
    public void testAddQuestionInQuizAndDeleteQuestion(){

    }

}
