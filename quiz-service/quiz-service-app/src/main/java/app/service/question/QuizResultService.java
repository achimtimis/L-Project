package app.service.question;

import app.domain.answers.AnswerEntity;
import app.domain.answers.QuizResponseEntity;
import app.domain.questions.QuestionCorrectAnswer;
import app.domain.questions.QuestionEntity;
import app.domain.questions.QuizEntity;
import app.domain.results.ResultEntity;
import app.repository.IQuestionCorrectAnswerDao;
import app.repository.IQuizEntityDao;
import app.repository.IQuizResponseEntityDao;
import app.repository.IResultEntityDao;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import models.Answer;
import models.AnsweredQuestion;
import models.QuizStudentResultResponse;
import models.questions.Question;
import models.questions.QuestionOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by achy_ on 6/11/2017.
 */
@Service
@Transactional
@Slf4j
public class QuizResultService {

    @Autowired
    IQuizResponseEntityDao quizResponseEntityDao;

    @Autowired
    IQuestionCorrectAnswerDao questionCorrectAnswerDao;

    @Autowired
    IResultEntityDao resultEntityDao;

    @Autowired
    IQuizEntityDao quizEntityDao;

    @Autowired
    private AnswerService answerService;

    @Autowired
    QuestionService questionService;

    Logger analytics = LoggerFactory.getLogger("analytics");



    public List<QuizResponseEntity> getQuizesToCorrect(String creatorId, Long quizId){
        List<QuizResponseEntity> quizes = new ArrayList<>();
        QuizEntity quizEntity = quizEntityDao.findByCreatorIdAndId(creatorId, quizId);
        return quizResponseEntityDao.findByQuiz(quizEntity).stream()
                .filter(quizResponseEntity -> quizResponseEntity.isCorrected() == false)
                .collect(Collectors.toList());
    }


    public ResultEntity getQuizResultByQuizResponseId(Long quizResponseId){
        QuizResponseEntity quizResponseEntity = quizResponseEntityDao.findOne(quizResponseId);
        return resultEntityDao.findByQuizResponse(quizResponseEntity);
    }

    public ResultEntity getQuizResult(QuizResponseEntity quizResponseEntity){
        ResultEntity result = null;
        switch (quizResponseEntity.getQuiz().getQuizType()){
            case INPUT_QUESTION:
                result = getInputQuizResult(quizResponseEntity);
                break;
            case MULTIPLE_ANSWERS:
                result = getOptionsOnlyQuizResult(quizResponseEntity);
                break;
            case INPUT_QUESTION_TIMED:
                result = getInputQuizResult(quizResponseEntity);
                break;
            case SINGLE_ANSWER:
                result = getOptionsOnlyQuizResult(quizResponseEntity);
                break;
            case MULTIPLE_ANSWER_AND_INPUT:
                result = getOptionsAndInputQuizResult(quizResponseEntity);
                break;
            case MULTIPLE_ANSWER_AND_INPUT_TIMED:
                result = getOptionsAndInputQuizResult(quizResponseEntity);
                break;
            case TIMED_MULTIPLE_ANSWER:
                result = getOptionsOnlyQuizResult(quizResponseEntity);
                break;
            case TIMED_SINGLE_ANSWER:
                result = getOptionsOnlyQuizResult(quizResponseEntity);
                break;

            default:
                throw new IllegalArgumentException("invalid quiz type");
        }
        return result;
    }

    private ResultEntity getOptionsAndInputQuizResult(QuizResponseEntity quizResponseEntity) {
        ResultEntity result = null;
        result = getInputQuizResult(quizResponseEntity);
        quizResponseEntity.setCorrected(false);
        quizResponseEntityDao.save(quizResponseEntity);
        return result;
    }

    private ResultEntity getInputQuizResult(QuizResponseEntity quizResponseEntity) {
        ResultEntity resultEntity = null;
        if (quizResponseEntity.isCorrected()){
            resultEntity = resultEntityDao.findByQuizResponse(quizResponseEntity);
        }
        return resultEntity;
    }


    public ResultEntity getOptionsOnlyQuizResult(QuizResponseEntity quizResponseEntity) {
        Gson gson = new Gson();
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setTotalDuration(quizResponseEntity.getTime());
        int totalScore = 0;
        for (AnswerEntity answer : quizResponseEntity.getAnswers()) {
            QuestionEntity questionEntity = answer.getQuizQuestion();
            QuestionCorrectAnswer questionCorrectAnswer = questionEntity.getQuestionCorrectAnswer();
            if (areAnswersCorrect(answer.getOption_responses(), questionCorrectAnswer.getValidAnswers())) {

                analytics.debug("{}", gson.toJson(new LogQuestionModel(questionEntity.getId(), quizResponseEntity.getQuiz().getId(), quizResponseEntity.getQuiz().getQuizType().toString(),
                        questionEntity.getQuestionText(),
                        questionEntity.getScore(), true, quizResponseEntity.getUserId())));
                totalScore += questionEntity.getScore();
                resultEntity.getScorePerQuestion().put(questionEntity.getId(), questionEntity.getScore());
            } else {
                resultEntity.getScorePerQuestion().put(questionEntity.getId(), 0d);
                analytics.debug("{}", gson.toJson(new LogQuestionModel(questionEntity.getId(), quizResponseEntity.getQuiz().getId(), quizResponseEntity.getQuiz().getQuizType().toString(),
                        questionEntity.getQuestionText(),
                        questionEntity.getScore(), false, quizResponseEntity.getUserId())));
            }
        }

        resultEntity.setQuizResponse(quizResponseEntity);
        resultEntity.setExtraFeedback("good job");
        resultEntity.setTotalScore(totalScore);
        if (totalScore >= quizResponseEntity.getQuiz().getMinimumScoreToPass()) {
            resultEntity.setPassed(true);
        }
        quizResponseEntity.setCorrected(true);
        quizResponseEntityDao.saveAndFlush(quizResponseEntity);
        return resultEntityDao.save(resultEntity);

    }

    private boolean areAnswersCorrect(List<Integer> option_responses, List<Integer> validAnswers) {
        boolean result = true;
        if (option_responses.size() != validAnswers.size()){
            result = false;
        }
        for (Integer i : option_responses) {
            if (!validAnswers.contains(i)) {
                result = false;
            }
        }
        return result;
    }

    public ResultEntity saveResult(ResultEntity resultEntity) {
        return resultEntityDao.saveAndFlush(resultEntity);
    }

    public List<ResultEntity> getQuizResultsForStudent(String student_id) {
        List<ResultEntity> resultEntities= new ArrayList<>();
        List<QuizResponseEntity> quizResponses = quizResponseEntityDao.findByUserId(student_id);
        for (QuizResponseEntity qre : quizResponses){
            if (qre.isCorrected()) {
                ResultEntity r = resultEntityDao.findByQuizResponse(qre);
                if (r != null) {
                    resultEntities.add(r);
                }
            }
        }
        return resultEntities;
    }
    public QuizStudentResultResponse mapResultResponse(ResultEntity quiz_result, String student_id) {
        QuizStudentResultResponse result = new QuizStudentResultResponse();
        result.setPassed(quiz_result.isPassed());
        result.setId(quiz_result.getId());
        result.setTotalScore(quiz_result.getTotalScore());
        result.setScore(quiz_result.getTotalScore()); //todo ?
        result.setCorrected(true);
        result.setQuiz_id(null);
        List<AnsweredQuestion> answeredQuestions = new ArrayList<>();
        for (Long question_id : quiz_result.getScorePerQuestion().keySet()) {
            QuestionEntity questionEntity = questionService.getQuestionById(question_id);
            AnsweredQuestion answeredQuestion = new AnsweredQuestion();
            answeredQuestion.setScore(quiz_result.getScorePerQuestion().get(question_id));
            AnswerEntity answerEntity = getAnswerEntity(questionEntity, answeredQuestion, student_id);

            Question question = getQuestionModel(questionEntity, answerEntity);
            answeredQuestion.setQuestion(question);
            answeredQuestion.setMaxScore(questionEntity.getScore());
            answeredQuestion.setObservation(quiz_result.getObservations().get(question_id));
            answeredQuestions.add(answeredQuestion);
        }
        result.setAnsweredQuestions(answeredQuestions);
        result.setExtraFeedback(quiz_result.getExtraFeedback());
        return result;

    }
    public AnswerEntity getAnswerEntity(QuestionEntity questionEntity, AnsweredQuestion answeredQuestion, String student_id) {
        AnswerEntity answerEntity = answerService.getAnswerByQuestion(questionEntity, student_id);
        Answer answer = new Answer();
        answer.setUserId(answerEntity.getCiamUserId());
        answer.setChosenOptions(answerEntity.getOption_responses());
        answer.setInputResponse(answerEntity.getInput_response());
        answer.setQuestionId(questionEntity.getId());
        answeredQuestion.setAnswer(answer);
        return answerEntity;
    }

    private Question getQuestionModel(QuestionEntity questionEntity, AnswerEntity answerEntity) {
        Question question = new Question();
        question.setId(questionEntity.getId());
        List<QuestionOptions> questionOptions = new ArrayList<>();
        for (int key : questionEntity.getOptions().keySet()) {
            questionOptions.add(new QuestionOptions(key, questionEntity.getOptions().get(key)));
        }
        question.setQuestionOptions(questionOptions);
        question.setInput(false);
        question.setQuestionText(questionEntity.getQuestionText());
        question.setInputField("");
        question.setScore(questionEntity.getScore());
        question.setQuestionId(questionEntity.getId());
        question.setChosenAnswer(answerEntity.getOption_responses()); //todo : wtf
        return question;
    }
}
