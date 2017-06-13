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
import lombok.extern.slf4j.Slf4j;
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
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setTotalDuration(quizResponseEntity.getTime());
        int totalScore = 0;
        for (AnswerEntity answer : quizResponseEntity.getAnswers()) {
            QuestionEntity questionEntity = answer.getQuizQuestion();
            QuestionCorrectAnswer questionCorrectAnswer = questionEntity.getQuestionCorrectAnswer();
            if (areAnswersCorrect(answer.getOption_responses(), questionCorrectAnswer.getValidAnswers())) {
                analytics.debug("answer: {}", new LogQuestionModel(questionEntity.getId(), questionEntity.getQuestionText(),
                        questionEntity.getScore(), true, quizResponseEntity.getUserId()));
                totalScore += questionEntity.getScore();
                resultEntity.getScorePerQuestion().put(questionEntity.getId(), questionEntity.getScore());
            } else {
                resultEntity.getScorePerQuestion().put(questionEntity.getId(), 0d);
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
}
