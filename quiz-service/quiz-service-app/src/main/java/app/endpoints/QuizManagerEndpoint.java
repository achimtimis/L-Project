package app.endpoints;

import app.domain.answers.AnswerEntity;
import app.domain.answers.QuizResponseEntity;
import app.domain.questions.QuestionCorrectAnswer;
import app.domain.questions.QuestionEntity;
import app.domain.questions.QuizEntity;
import app.repository.IQuestionCorrectAnswerDao;
import app.repository.IQuizEntityDao;
import app.repository.IQuizResponseEntiyDao;
import app.service.question.QuestionService;
import endpoints.IQuizManagerEndpoint;
import models.Answer;
import models.QuizCreationRequest;
import models.QuizRequest;
import models.QuizResponseRequest;
import models.questions.Question;
import models.questions.QuestionCreatedWithAnswer;
import models.questions.QuestionOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by achy_ on 6/10/2017.
 */
@RestController
@CrossOrigin
public class QuizManagerEndpoint implements IQuizManagerEndpoint {

    @Autowired
    private IQuizEntityDao quizEntityDao;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private IQuestionCorrectAnswerDao correctAnswerDao;

    @Autowired
    private IQuizResponseEntiyDao iQuizResponseEntiyDao;

    @Override
    public QuizCreationRequest createQuiz(@RequestBody QuizCreationRequest q) {

        QuizEntity quizEntity = new QuizEntity();
        quizEntity.setQuizType(q.getQuizType());
        quizEntity.setTopic(q.getTopic());

        List<QuestionEntity> questions = new ArrayList<>();
        for (QuestionCreatedWithAnswer qca: q.getQuestionCreatedWithAnswers()){
            QuestionEntity questionEntity = new QuestionEntity();
            Map<Integer, String> options = new HashMap<>();
            for (QuestionOptions qo : qca.getQuestionOptions()){
                options.put(qo.getKey(), qo.getValue());
            }
            questionEntity.setQuestionText(qca.getQuestionText());
            questionEntity.setOptions(options);


            QuestionCorrectAnswer qa = new QuestionCorrectAnswer();
            qa.setValidAnswers(qca.getCorrectAnswers());
            questionEntity.setQuestionCorrectAnswer(qa);
            questions.add(questionEntity);
        }
        quizEntity.setQuiz_questions(questions);
        quizEntity.setTimer(q.getTimer());

        QuizEntity quizEntity1 = quizEntityDao.save(quizEntity);
//        for (QuestionEntity questionEntity : quizEntity1.getQuiz_questions()){
//            QuestionCorrectAnswer qca = new QuestionCorrectAnswer();
//            qca.setQuestion(questionEntity);
//            qca.setValidAnswers(q.getQuestionCreatedWithAnswers());
//        }

    return null;
    }

    @Override
    public QuestionCreatedWithAnswer saveQuestion(@RequestBody QuestionCreatedWithAnswer q) {
        QuestionEntity questionEntity = new QuestionEntity();
        Map<Integer, String> options = new HashMap<>();
        for (QuestionOptions qo : q.getQuestionOptions()){
            options.put(qo.getKey(), qo.getValue());
        }
        questionEntity.setQuestionText(q.getQuestionText());
        questionEntity.setOptions(options);


        QuestionCorrectAnswer qa = new QuestionCorrectAnswer();
        qa.setValidAnswers(q.getCorrectAnswers());

        questionEntity.setQuestionCorrectAnswer(qa);
        questionService.saveOrUpdateQuestion(questionEntity);
        return null;
    }

    @Override
    public QuizRequest getQuizById(@PathVariable(value = "id") Long id) {
        QuizRequest quizRequest = new QuizRequest();
        QuizEntity quizEntity = quizEntityDao.findOne(id);
        quizRequest.setQuiz_id(quizEntity.getId());
        quizRequest.setTimer(quizEntity.getTimer());
        quizRequest.setTimed(false);
        quizRequest.setQuizType(quizEntity.getQuizType());
        List<Question> questions = new ArrayList<>();
        for (QuestionEntity questionEntity : quizEntity.getQuiz_questions()){
            Question question = new Question();
            question.setId(questionEntity.getId());
            question.setChosenAnswer(new ArrayList<>());
            question.setQuestionText(questionEntity.getQuestionText());
            question.setInput(false); // todo: determine is timed by quiztyoe
            List<QuestionOptions> qo = new ArrayList<>();
            for (Integer key : questionEntity.getOptions().keySet()){
                qo.add(new QuestionOptions(Integer.valueOf(key), questionEntity.getOptions().get(key)));
            }
            question.setQuestionOptions(qo);
            questions.add(question);
        }
        quizRequest.setQuestions(questions);
        quizRequest.setTopic(quizEntity.getTopic());
        return quizRequest;
    }

    @Override
    public QuizResponseRequest saveQuizResponse(@RequestBody QuizResponseRequest quizResponseRequest) {
        QuizResponseEntity quizResponseEntity = new QuizResponseEntity();
        quizResponseEntity.setUserId(quizResponseRequest.getUserId());
        quizResponseEntity.setTime(quizResponseRequest.getTime());
        QuizEntity qe = quizEntityDao.findOne(quizResponseRequest.getQuizId());
        quizResponseEntity.setQuiz(qe);
        List<AnswerEntity> answers = new ArrayList<>();
        for (Answer a : quizResponseRequest.getAnswerList()){
            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setInput_response(a.getInputResponse());
            answerEntity.setQuizResponse(null); //todo : wtf is this
            answerEntity.setQuiz_question(questionService.getQuestionById(a.getQuestionId()));
            answerEntity.setOption_responses(a.getChosenOptions());
            answerEntity.setCiamUserId(a.getUserId());
            answers.add(answerEntity);
        }
        quizResponseEntity.setAnswers(answers);
        iQuizResponseEntiyDao.save(quizResponseEntity);
        return null;
    }
}
