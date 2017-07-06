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
    public List<QuizRequest> getAllQuizesOfAStudent(@PathVariable(name = "studentid") String studentid) {
        if (userServiceFeign.getUser(studentid) != null) {
            return quizServiceFeign.getAllQuizesOfAStudent(studentid);
        } else {
            throw new IllegalArgumentException("the user does not exist");
        }
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
    public void saveQuizFailedResponse(@PathVariable(name = "userid") String userid,
                                       @PathVariable(name = "quizid") Long quizid) {
        if (userServiceFeign.getUser(userid) != null) {
            quizServiceFeign.saveQuizFailedResponse(userid, quizid);
        } else {
            throw new IllegalArgumentException("the user does not exist");
        }
    }

    @Override
    public QuizStudentResultResponse getQuizResultForStudent(@PathVariable(name = "student_id") String studentId,
                                                             @PathVariable(name = "quiz_response_id") Long quiz_response_id) {
        if (userServiceFeign.getUser(studentId) != null) {
            return quizServiceFeign.getQuizResultForStudent(studentId, quiz_response_id);
        } else {
            throw new IllegalArgumentException("the user does not exist");
        }
    }

    @Override
    public List<QuizStudentResultResponse> getAllQuizResultsForStudent(@PathVariable(name = "student_id") String student_id) {
        if (userServiceFeign.getUser(student_id) != null) {
            return quizServiceFeign.getAllQuizResultsForStudent(student_id);
        } else {
            throw new IllegalArgumentException("the user does not exist");
        }
    }

    @Override
    public List<QuizStudentResultResponse> getAllQuizResultsByProfessor(@PathVariable(name = "prof_id") String prof_id) {
        if (userServiceFeign.getUser(prof_id) != null) {
            return quizServiceFeign.getAllQuizResultsByProfessor(prof_id);
        } else {
            throw new IllegalArgumentException("the user does not exist");
        }

    }

    @Override
    public List<QuizToCorrectRequest> getListOfQuizesToCorrect(@PathVariable(name = "userId") String userId) {
        return quizServiceFeign.getListOfQuizesToCorrect(userId);
    }

    @Override
    public QuizToCorrectRequest getQuizResponseToGradeById(@PathVariable(name = "responseid") Long responseid) {
        return quizServiceFeign.getQuizResponseToGradeById(responseid);
    }

    @Override
    public void saveQuizResponseEvaluation(@RequestBody QuizToCorrectRequest quizToCorrectRequest) {
        quizServiceFeign.saveQuizResponseEvaluation(quizToCorrectRequest);
    }

    @Override
    public QuizStudentResultResponse getQuizResutByid(@PathVariable("resultid") Long resultid) {
        return quizServiceFeign.getQuizResutByid(resultid);
    }


}
