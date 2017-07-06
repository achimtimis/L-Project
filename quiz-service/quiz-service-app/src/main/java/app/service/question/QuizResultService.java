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
import models.*;
import models.questions.Question;
import models.questions.QuestionOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
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


    public List<QuizResponseEntity> getQuizesToCorrect(String creatorId) {
        List<QuizResponseEntity> quizes = new ArrayList<>();
        List<QuizEntity> quizEntities = quizEntityDao.findByCreatorId(creatorId);
        quizEntities.stream().forEach(quizEntity ->
                quizResponseEntityDao.findByQuiz(quizEntity).stream()
                        .filter(quizResponseEntity -> quizResponseEntity.isCorrected() == false)
                        .forEach(quizes::add));
        return quizes;
    }


    public ResultEntity getQuizResultByQuizResponseId(Long quizResponseId) {
        QuizResponseEntity quizResponseEntity = quizResponseEntityDao.findOne(quizResponseId);
        return resultEntityDao.findByQuizResponse(quizResponseEntity);
    }

    public ResultEntity getQuizResult(QuizResponseEntity quizResponseEntity) {
        ResultEntity result = null;
        switch (quizResponseEntity.getQuiz().getQuizType()) {
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
        if (quizResponseEntity.isCorrected()) {
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
                analytics.debug("{}", gson.toJson(new LogQuestionModel(quizResponseEntity.getQuiz().getName(), quizResponseEntity.getQuiz().getQuizType().toString(),
                        questionEntity.getQuestionText(),
                        questionEntity.getScore(), true, quizResponseEntity.getUserId(), quizResponseEntity.getQuiz().getCreatorId())));
                totalScore += questionEntity.getScore();
                resultEntity.getScorePerQuestion().put(questionEntity.getId(), questionEntity.getScore());
            } else {
                resultEntity.getScorePerQuestion().put(questionEntity.getId(), 0d);
                analytics.debug("{}", gson.toJson(new LogQuestionModel(quizResponseEntity.getQuiz().getName(), quizResponseEntity.getQuiz().getQuizType().toString(),
                        questionEntity.getQuestionText(),
                        questionEntity.getScore(), false, quizResponseEntity.getUserId(), quizResponseEntity.getQuiz().getCreatorId())));
            }
        }

        resultEntity.setQuizResponse(quizResponseEntity);
        resultEntity.setTotalScore(totalScore);
        if (totalScore >= quizResponseEntity.getQuiz().getMinimumScoreToPass()) {
            resultEntity.setPassed(true);
            resultEntity.setExtraFeedback("good job");
        } else {
            resultEntity.setPassed(false);
            resultEntity.setExtraFeedback("quiz failed");
        }
        quizResponseEntity.setCorrected(true);
        quizResponseEntityDao.saveAndFlush(quizResponseEntity);
        return resultEntityDao.save(resultEntity);

    }

    private boolean areAnswersCorrect(List<Integer> option_responses, List<Integer> validAnswers) {
        boolean result = true;
        if (option_responses.size() != validAnswers.size()) {
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
        List<ResultEntity> resultEntities = new ArrayList<>();
        List<QuizResponseEntity> quizResponses = quizResponseEntityDao.findByUserId(student_id);
        for (QuizResponseEntity qre : quizResponses) {
            if (qre.isCorrected()) {
                ResultEntity r = resultEntityDao.findByQuizResponse(qre);
                if (r != null) {
                    resultEntities.add(r);
                }
            }
        }
        return resultEntities;
    }

    public List<ResultEntity> getQuizResultsByProfessor(String proffId) {
        List<ResultEntity> resultEntities = new ArrayList<>();
        List<QuizEntity> quizesByProfessor = quizEntityDao.findByCreatorId(proffId);
        quizesByProfessor
                .stream()
                .forEach(quizEntity -> quizResponseEntityDao.findByQuiz(quizEntity)
                        .stream()
                        .filter(quizResponseEntity -> quizResponseEntity.isCorrected())
                        .forEach(qre -> resultEntities.add(resultEntityDao.findByQuizResponse(qre))));
        return resultEntities;
    }

    public QuizStudentResultResponse mapResultResponse(ResultEntity quiz_result, String student_id) {
        QuizStudentResultResponse result = new QuizStudentResultResponse();
        result.setPassed(quiz_result.isPassed());
        result.setId(quiz_result.getId());
        result.setTotalScore(quiz_result.getQuizResponse().getQuiz().getTotalScore());
        result.setScore(quiz_result.getTotalScore()); //todo ?
        result.setCorrected(true);
        result.setQuiz_name(quiz_result.getQuizResponse().getQuiz().getName());
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
        result.setTotalDuration(quiz_result.getQuizResponse().getTime());
        result.setAnsweredQuestions(answeredQuestions);
        result.setExtraFeedback(quiz_result.getExtraFeedback());
        result.setStudent(quiz_result.getQuizResponse().getUserId());
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

    public void saveFailedQuiz(String userid, Long quizid) {
        QuizEntity quiz = quizEntityDao.getOne(quizid);
        QuizResponseEntity quizResponseEntity = new QuizResponseEntity();
        quizResponseEntity.setAnswers(new ArrayList<>());
        quizResponseEntity.setCorrected(true);
        quizResponseEntity.setUserId(userid);
        quizResponseEntity.setQuiz(quiz);
        quizResponseEntity.setTime(quiz.getTimer() + 1);
        quizResponseEntity.setWasFinishedInTime(false);
        QuizResponseEntity saved = quizResponseEntityDao.save(quizResponseEntity);
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setQuizResponse(saved);
        resultEntity.setExtraFeedback("Be faster next time");
        resultEntity.setTotalDuration(quiz.getTimer() + 1);
        resultEntity.setTotalScore(0);
        resultEntity.setObservations(new HashMap<>());
        resultEntity.setScorePerQuestion(new HashMap<>());
        resultEntity.setRecomandations("Be faser next time");
        resultEntity.setPassed(false);
        resultEntityDao.save(resultEntity);
        logQuizFailed("Quiz not finished in time", resultEntity);
    }

    private void logQuizFailed(String message, ResultEntity resultEntity) {
        Gson gson = new Gson();
        analytics.debug("{}", gson.toJson(new LogQuestionModel(resultEntity.getQuizResponse().getQuiz().getName(),
                resultEntity.getQuizResponse().getQuiz().getQuizType().toString(),
                message, 0, false, resultEntity.getQuizResponse().getUserId(), resultEntity.getQuizResponse().getQuiz().getCreatorId())));

    }


    private AnswerWithQuestion getAnswerWithQuestionModel(AnswerEntity answerEntity) {
        QuestionEntity questionEntity = questionService.getQuestionById(answerEntity.getQuizQuestion().getId());
        AnswerWithQuestion answerWithQuestion = new AnswerWithQuestion();
        answerWithQuestion.setId(answerEntity.getId());
        answerWithQuestion.setUserId(answerEntity.getCiamUserId());
        answerWithQuestion.setChosenOptions(answerEntity.getOption_responses());
        answerWithQuestion.setInputResponse(answerEntity.getInput_response());
        answerWithQuestion.setQuestion(getQuestionModel(questionEntity, answerEntity));
        return answerWithQuestion;
    }

    public QuizToCorrectRequest mapQuizToCorrectModel(QuizResponseEntity quizResponseEntity) {
        QuizToCorrectRequest quizToCorrect = new QuizToCorrectRequest();
        quizToCorrect.setId(quizResponseEntity.getId());
        quizToCorrect.setUserId(quizResponseEntity.getUserId());
        quizToCorrect.setQuiz_name(quizResponseEntity.getQuiz().getName());
        quizToCorrect.setTime(quizResponseEntity.getTime());
        quizToCorrect.setQuizId(quizResponseEntity.getQuiz().getId());
        quizToCorrect.setAnswerList(quizResponseEntity.getAnswers().stream()
                .map(answerEntity -> getAnswerWithQuestionModel(answerEntity)).collect(Collectors.toList()));
        quizToCorrect.setMinScoreToPass(quizResponseEntity.getQuiz().getMinimumScoreToPass());
        quizToCorrect.setTotalScore(quizResponseEntity.getQuiz().getTotalScore());
        return quizToCorrect;
    }

    public QuizResponseEntity getQuizToCorrect(Long responseid) {
        return this.quizResponseEntityDao.findOne(responseid);
    }

    public void logInputQuizResult(ResultEntity resultEntity) {
        Gson gson = new Gson();
        QuizResponseEntity quizResponseEntity = resultEntity.getQuizResponse();
        for (QuestionEntity questionEntity : quizResponseEntity.getQuiz().getQuiz_questions()) {
            QuestionCorrectAnswer questionCorrectAnswer = questionEntity.getQuestionCorrectAnswer();
            if (questionEntity.getScore() == resultEntity.getScorePerQuestion().get(questionEntity.getId())) {
                analytics.debug("{}", gson.toJson(new LogQuestionModel(quizResponseEntity.getQuiz().getName(), quizResponseEntity.getQuiz().getQuizType().toString(),
                        questionEntity.getQuestionText(),
                        questionEntity.getScore(), true, quizResponseEntity.getUserId(), quizResponseEntity.getQuiz().getCreatorId())));
            } else {
                analytics.debug("{}", gson.toJson(new LogQuestionModel(quizResponseEntity.getQuiz().getName(), quizResponseEntity.getQuiz().getQuizType().toString(),
                        questionEntity.getQuestionText(),
                        questionEntity.getScore(), false, quizResponseEntity.getUserId(), quizResponseEntity.getQuiz().getCreatorId())));
            }

        }
    }

    public QuizStudentResultResponse getQuizResultById(Long resultid) {
        ResultEntity resultEntity = this.resultEntityDao.findOne(resultid);
        return mapResultResponse(resultEntity, resultEntity.getQuizResponse().getUserId());
    }
}
