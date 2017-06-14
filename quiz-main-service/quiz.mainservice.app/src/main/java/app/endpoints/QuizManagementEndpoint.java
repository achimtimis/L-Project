package app.endpoints;

import app.client.IQuizServiceFeign;
import app.client.IUserServiceFeign;
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

    @Autowired
    private IUserServiceFeign userServiceFeign;

    @Override
    public QuizCreationRequest createQuiz(@RequestBody QuizCreationRequest quizCreationRequest) {
        if (userServiceFeign.getUser(quizCreationRequest.getCreatorId()) != null) {
            return quizServiceFeign.createQuiz(quizCreationRequest);
        } else {
            throw new IllegalArgumentException("the user does not exist");
        }
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
        if (userServiceFeign.getUser(quizResponseRequest.getUserId()) != null) {
            return quizServiceFeign.saveQuizResponse(quizResponseRequest);
        } else {
            throw new IllegalArgumentException("the user does not exist");
        }
    }

    @Override
    public QuizStudentResultResponse getQuizResultForStudent(@PathVariable(name = "student_id") String studentId, @PathVariable(name = "quiz_id") Long quizId) {
        if (userServiceFeign.getUser(studentId) != null) {
            return quizServiceFeign.getQuizResultForStudent(studentId, quizId);
        } else {
            throw new IllegalArgumentException("the user does not exist");
        }
    }

    @Override
    public List<QuizToCorrectRequest> getListOfQuizesToCorrect(@PathVariable(name = "userId") String professorId, @PathVariable(name = "quizId") Long quizId) {
        if (userServiceFeign.getUser(professorId) != null) {
            return quizServiceFeign.getListOfQuizesToCorrect(professorId, quizId);
        } else {
            throw new IllegalArgumentException("the user does not exist");
        }
    }
}
