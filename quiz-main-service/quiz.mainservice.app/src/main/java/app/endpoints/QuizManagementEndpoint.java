package app.endpoints;

import app.client.IQuizServiceFeign;
import endpoints.IQuizManagerEndpoint;
import models.*;
import models.questions.QuestionCreatedWithAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by achy_ on 6/13/2017.
 */
@RestController
@CrossOrigin
public class QuizManagementEndpoint implements IQuizManagerEndpoint {


    @Autowired
    IQuizServiceFeign quizServiceFeign;

    @Override
    public QuizCreationRequest createQuiz(@RequestBody QuizCreationRequest quizCreationRequest) {
        return quizServiceFeign.createQuiz(quizCreationRequest);
    }

    @Override
    public QuestionCreatedWithAnswer saveQuestion(@RequestBody QuestionCreatedWithAnswer questionCreatedWithAnswer) {
        return quizServiceFeign.saveQuestion(questionCreatedWithAnswer);
    }

    @Override
    public QuizRequest getQuizById(@PathVariable("id") Long aLong) {
        return quizServiceFeign.getQuizById(aLong);
    }

    @Override
    public QuizResponseRequest saveQuizResponse(@RequestBody QuizResponseRequest quizResponseRequest) {
        return quizServiceFeign.saveQuizResponse(quizResponseRequest);
    }

    @Override
    public QuizStudentResultResponse getQuizResultForStudent(@PathVariable(name = "student_id") String s, @PathVariable(name = "quiz_id") Long aLong) {
        return quizServiceFeign.getQuizResultForStudent(s, aLong);
    }

    @Override
    public List<QuizToCorrectRequest> getListOfQuizesToCorrect(@PathVariable(name = "userId") String s, @PathVariable(name = "quizId") Long aLong) {
        return quizServiceFeign.getListOfQuizesToCorrect(s, aLong);
    }
}
