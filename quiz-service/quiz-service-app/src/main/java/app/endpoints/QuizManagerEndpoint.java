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
//        (origins = "http://localhost:8808")
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
        quizEntity.setName(q.getName());
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
        quizEntity.setTimed(q.isTimed());
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
    public List<QuizRequest> getAllQuizesOfAStudent(@PathVariable(name = "studentid") String studentid) {
        return quizService.getAllQuizesOfAStudent(studentid).stream().map(quizEntity -> getQuizRequestModel(quizEntity))
                .collect(Collectors.toList());
    }

    @Override
    public QuizRequest getQuizById(@PathVariable(value = "id") Long id) {
        QuizEntity quizEntity = quizService.findOne(id);
        return getQuizRequestModel(quizEntity);
    }

    private QuizRequest getQuizRequestModel(QuizEntity quizEntity) {
        QuizRequest quizRequest = new QuizRequest();
        quizRequest.setQuiz_id(quizEntity.getId());
        quizRequest.setTimer(quizEntity.getTimer());
        quizRequest.setTimed(quizEntity.isTimed());
        quizRequest.setName(quizEntity.getName());
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
            question.setQuestionId(questionEntity.getId());
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
    public void saveQuizFailedResponse(@PathVariable(name = "userid") String userid, @PathVariable(name = "quizid") Long quizid) {
         quizResultService.saveFailedQuiz(userid, quizid);
    }

    @Override
    public QuizStudentResultResponse getQuizResultForStudent(@PathVariable(name = "student_id") String student_id,
                                                             @PathVariable(name = "quiz_response_id") Long quiz_response_id) {
        return quizResultService.mapResultResponse(quizResultService.getQuizResultsForStudent(student_id)
                .stream().filter(resultEntity -> resultEntity.getId() == quiz_response_id).findFirst().get(), student_id);
    }


    @Override
    public List<QuizStudentResultResponse> getAllQuizResultsForStudent(@PathVariable(name = "student_id") String student_id) {
        List<ResultEntity> resultEntities = quizResultService.getQuizResultsForStudent(student_id);
        return resultEntities.stream().map(resultEntity -> quizResultService.mapResultResponse(resultEntity, student_id))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuizStudentResultResponse> getAllQuizResultsByProfessor(@PathVariable(name = "prof_id") String prof_id) {
        return quizResultService.getQuizResultsByProfessor(prof_id).stream()
                .map(resultEntity -> quizResultService.mapResultResponse(resultEntity, resultEntity.getQuizResponse().getUserId()))
                .collect(Collectors.toList());
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


    @Override
    public List<QuizToCorrectRequest> getListOfQuizesToCorrect(@PathVariable(name = "userId") String userId) {
        List<QuizToCorrectRequest> quizes = new ArrayList<>();
        List<QuizResponseEntity> quiz_responses = quizResultService.getQuizesToCorrect(userId);
        for (QuizResponseEntity quizResponseEntity : quiz_responses) {
            QuizToCorrectRequest quizToCorrect = quizResultService.mapQuizToCorrectModel(quizResponseEntity);
            quizes.add(quizToCorrect);

        }
        return quizes;
    }



    @Override
    public QuizToCorrectRequest getQuizResponseToGradeById(@PathVariable(name = "responseid") Long responseid) {
        return quizResultService.mapQuizToCorrectModel(quizResultService.getQuizToCorrect(responseid));
    }

    @Override
    public void saveQuizResponseEvaluation(@RequestBody QuizToCorrectRequest quizToCorrectRequest) {
        QuizResponseEntity quizResponseEntity = quizService.getQuizResponseEntityById(quizToCorrectRequest.getId());
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
