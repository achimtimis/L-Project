package app.endpoints;

import app.domain.answers.AnswerEntity;
import app.domain.answers.QuizResponseEntity;
import app.domain.questions.QuestionCorrectAnswer;
import app.domain.questions.QuestionEntity;
import app.domain.questions.QuizEntity;
import app.domain.results.ResultEntity;
import app.service.question.AnswerService;
import app.service.question.QuestionService;
import app.service.question.QuizResultService;
import app.service.question.QuizService;
import endpoints.IQuizManagerEndpoint;
import models.*;
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
import java.util.stream.Collectors;

/**
 * Created by achy_ on 6/10/2017.
 */
@RestController
@CrossOrigin
public class QuizManagerEndpoint implements IQuizManagerEndpoint {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizResultService quizResultService;

    @Autowired
    private AnswerService answerService;


    @Override
    public QuizCreationRequest createQuiz(@RequestBody QuizCreationRequest q) {

        QuizEntity quizEntity = new QuizEntity();
        quizEntity.setQuizType(q.getQuizType());
        quizEntity.setTopic(q.getTopic());

        List<QuestionEntity> questions = new ArrayList<>();
        for (QuestionCreatedWithAnswer qca : q.getQuestionCreatedWithAnswers()) {
            QuestionEntity questionEntity = new QuestionEntity();
            Map<Integer, String> options = new HashMap<>();
            for (QuestionOptions qo : qca.getQuestionOptions()) {
                options.put(qo.getKey(), qo.getValue());
            }
            questionEntity.setQuestionText(qca.getQuestionText());
            questionEntity.setOptions(options);
            questionEntity.setScore(qca.getScore());

            QuestionCorrectAnswer qa = new QuestionCorrectAnswer();
            qa.setValidAnswers(qca.getCorrectAnswers());
            questionEntity.setQuestionCorrectAnswer(qa);
            questions.add(questionEntity);
        }
        quizEntity.setQuiz_questions(questions);
        quizEntity.setTimer(q.getTimer());
        quizEntity.setMinimumScoreToPass(q.getMinScoreToPass());
        quizEntity.setCreatorId(q.getCreatorId());

        QuizEntity quizEntity1 = quizService.save(quizEntity);

        return null;
    }

    @Override
    public QuestionCreatedWithAnswer saveQuestion(@RequestBody QuestionCreatedWithAnswer q) {
        QuestionEntity questionEntity = new QuestionEntity();
        Map<Integer, String> options = new HashMap<>();
        for (QuestionOptions qo : q.getQuestionOptions()) {
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
        QuizEntity quizEntity = quizService.findOne(id);
        quizRequest.setQuiz_id(quizEntity.getId());
        quizRequest.setTimer(quizEntity.getTimer());
        quizRequest.setTimed(false);
        quizRequest.setQuizType(quizEntity.getQuizType());
        List<Question> questions = new ArrayList<>();
        for (QuestionEntity questionEntity : quizEntity.getQuiz_questions()) {
            Question question = new Question();
            question.setId(questionEntity.getId());
            question.setChosenAnswer(new ArrayList<>());
            question.setQuestionText(questionEntity.getQuestionText());
            question.setInput(false); // todo: determine is timed by quiztyoe
            question.setScore(questionEntity.getScore());
            List<QuestionOptions> qo = new ArrayList<>();
            for (Integer key : questionEntity.getOptions().keySet()) {
                qo.add(new QuestionOptions(Integer.valueOf(key), questionEntity.getOptions().get(key)));
            }
            question.setQuestionOptions(qo);
            questions.add(question);
        }
        quizRequest.setQuestions(questions);
        quizRequest.setTopic(quizEntity.getTopic());
        quizRequest.setMinScoreToPass(quizEntity.getMinimumScoreToPass());
        quizRequest.setTotalScore(quizEntity.getMinimumScoreToPass());
        return quizRequest;
    }

    @Override
    public QuizResponseRequest saveQuizResponse(@RequestBody QuizResponseRequest quizResponseRequest) {
        QuizResponseEntity quizResponseEntity = new QuizResponseEntity();
        QuizEntity qe = quizService.findOne(quizResponseRequest.getQuizId());
        if (quizService.getQuizResponseEntityByStudentIdAndQuizId(qe, quizResponseRequest.getUserId()) != null) {
            return null;
        }
        quizResponseEntity.setUserId(quizResponseRequest.getUserId());
        quizResponseEntity.setTime(quizResponseRequest.getTime());
        quizResponseEntity.setQuiz(qe);
        List<AnswerEntity> answers = new ArrayList<>();
        for (Answer a : quizResponseRequest.getAnswerList()) {
            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setInput_response(a.getInputResponse());
            answerEntity.setQuizResponse(null); //todo : wtf is this
            answerEntity.setQuizQuestion(questionService.getQuestionById(a.getQuestionId()));
            answerEntity.setOption_responses(a.getChosenOptions());
            answerEntity.setCiamUserId(a.getUserId());
            answers.add(answerEntity);
        }
        quizResponseEntity.setAnswers(answers);
        QuizResponseEntity saved = quizService.save(quizResponseEntity);
        quizResultService.getQuizResult(saved);
        return null;
    }

    @Override
    public QuizStudentResultResponse getQuizResultForStudent(@PathVariable(name = "student_id") String student_id, @PathVariable(name = "quiz_id") Long quiz_id) {
        QuizStudentResultResponse result = new QuizStudentResultResponse();
        //todo: check if student exists
        QuizResponseEntity quizResponseEntity = quizService.getQuizResponseEntityById(quiz_id);
        if (quizResponseEntity.isCorrected()) {
            //
            ResultEntity quiz_result = quizResultService.getQuizResultByQuizResponseId(quiz_id);
            result.setPassed(quiz_result.isPassed());
            result.setId(quiz_result.getId());
            result.setTotalScore(quizResponseEntity.getQuiz().getMinimumScoreToPass());
            result.setScore(quiz_result.getTotalScore()); //todo ?
            result.setCorrected(quizResponseEntity.isCorrected());
            result.setQuiz_id(quizResponseEntity.getQuiz().getId());
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
        } else {
            result = null;
        }
        return result;

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
        question.setChosenAnswer(answerEntity.getOption_responses()); //todo : wtf
        return question;
    }

    private AnswerEntity getAnswerEntity(QuestionEntity questionEntity, AnsweredQuestion answeredQuestion, String student_id) {
        AnswerEntity answerEntity = answerService.getAnswerByQuestion(questionEntity, student_id);
        Answer answer = new Answer();
        answer.setUserId(answerEntity.getCiamUserId());
        answer.setChosenOptions(answerEntity.getOption_responses());
        answer.setInputResponse(answerEntity.getInput_response());
        answer.setQuestionId(questionEntity.getId());
        answeredQuestion.setAnswer(answer);
        return answerEntity;
    }

    @Override
    public List<QuizToCorrectRequest> getListOfQuizesToCorrect(@PathVariable(name = "userId") String userId, @PathVariable(name = "quizId") Long quizId) {
        List<QuizToCorrectRequest> quizes = new ArrayList<>();
        List<QuizResponseEntity> quiz_responses = quizResultService.getQuizesToCorrect(userId, quizId);
        for (QuizResponseEntity quizResponseEntity : quiz_responses) {
            QuizToCorrectRequest quizToCorrect = new QuizToCorrectRequest();
            quizToCorrect.setId(quizResponseEntity.getId());
            quizToCorrect.setUserId(quizResponseEntity.getUserId());
            quizToCorrect.setTime(quizResponseEntity.getTime());
            quizToCorrect.setQuizId(quizResponseEntity.getQuiz().getId());
            quizToCorrect.setAnswerList(quizResponseEntity.getAnswers().stream()
                    .map(answerEntity -> getAnswerWithQuestionModel(answerEntity)).collect(Collectors.toList()));
            quizes.add(quizToCorrect);

        }
        return quizes;
    }

    @Override
    public void saveQuizResponseEvaluation(@RequestBody QuizToCorrectRequest quizToCorrectRequest) {
        QuizResponseEntity quizResponseEntity = quizService.getQuizResponseEntityById(quizToCorrectRequest.getQuizId());
        ResultEntity resultEntity = quizResultService.getQuizResult(quizResponseEntity);
        if (resultEntity != null) {
            // if both input and choose
            resultEntity.setPassed(quizToCorrectRequest.isPassed());
            resultEntity.setTotalScore(quizToCorrectRequest.getScore());
            resultEntity.setExtraFeedback(quizToCorrectRequest.getExtraFeedback());
            resultEntity.setTotalDuration(quizToCorrectRequest.getTime());
            Map<Long, Double> scorePerQuestion = new HashMap<>();
            Map<Long, String> observations = new HashMap<>();
            for (AnswerWithQuestion answer : quizToCorrectRequest.getAnswerList()) {
                scorePerQuestion.put(answer.getQuestion().getId(), answer.getGraded_score());
                observations.put(answer.getQuestion().getId(), answer.getObservation());
            }
            resultEntity.setExtraFeedback(quizToCorrectRequest.getExtraFeedback());
            quizResponseEntity.setCorrected(true);
            quizResultService.saveResult(resultEntity);
            quizService.saveOrUpdateQuizResponse(quizResponseEntity);
        } else {
            // only input
            resultEntity = new ResultEntity();
            resultEntity.setPassed(quizToCorrectRequest.isPassed());
            resultEntity.setTotalScore(quizToCorrectRequest.getScore());
            resultEntity.setExtraFeedback(quizToCorrectRequest.getExtraFeedback());
            resultEntity.setTotalDuration(quizToCorrectRequest.getTime());
            Map<Long, Double> scorePerQuestion = new HashMap<>();
            Map<Long, String> observations = new HashMap<>();
            for (AnswerWithQuestion answer : quizToCorrectRequest.getAnswerList()) {
                scorePerQuestion.put(answer.getQuestion().getId(), answer.getGraded_score());
                observations.put(answer.getQuestion().getId(), answer.getObservation());
            }
            resultEntity.setExtraFeedback(quizToCorrectRequest.getExtraFeedback());
            quizResponseEntity.setCorrected(true);
            QuizResponseEntity saved = quizService.saveOrUpdateQuizResponse(quizResponseEntity);
            resultEntity.setQuizResponse(saved);
            resultEntity = quizResultService.saveResult(resultEntity);

        }

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

    private Answer getAnswerModel(AnswerEntity answerEntity) {
        Answer answer = new Answer();
        answer.setId(answerEntity.getId());
        answer.setQuestionId(answerEntity.getQuizQuestion().getId());
        answer.setUserId(answerEntity.getCiamUserId());
        answer.setInputResponse(answerEntity.getInput_response());
        answer.setChosenOptions(answerEntity.getOption_responses());
        answer.setQuestionText(answerEntity.getQuizQuestion().getQuestionText());
        return answer;
    }


}
