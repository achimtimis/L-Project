package endpoints;

import models.*;
import models.questions.QuestionCreatedWithAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by achy_ on 6/10/2017.
 */
public interface IQuizManagerEndpoint {


    @RequestMapping(value = "/quizes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    QuizCreationRequest createQuiz(@RequestBody QuizCreationRequest q);

    @RequestMapping(value = "/quizAnswer", method = RequestMethod.POST)
    @ResponseBody
    QuestionCreatedWithAnswer saveQuestion(@RequestBody QuestionCreatedWithAnswer q);

    @RequestMapping(value = "/quizes/{studentid}", method = RequestMethod.GET)
    @ResponseBody
    List<QuizRequest> getAllQuizesOfAStudent(@PathVariable(name = "studentid") String userId);


    @RequestMapping(value = "quiz/{id}", method = RequestMethod.GET)
    @ResponseBody
    QuizRequest getQuizById(@PathVariable(value = "id") Long id);


    @RequestMapping(value = "quiz/response", method = RequestMethod.POST)
    @ResponseBody
    QuizResponseRequest saveQuizResponse(@RequestBody QuizResponseRequest quizResponseRequest);

    @RequestMapping(value = "quiz/response/failed/{userid}/{quizid}", method = RequestMethod.POST)
    @ResponseBody
    void saveQuizFailedResponse(@PathVariable(name = "userid") String userid, @PathVariable(name = "quizid") Long quizid);

    @RequestMapping(value = "quiz/result/{student_id}/{quiz_response_id}", method = RequestMethod.GET)
    @ResponseBody
    QuizStudentResultResponse getQuizResultForStudent(@PathVariable(name = "student_id") String student_id,
                                                      @PathVariable(name = "quiz_response_id") Long quiz_response_id);

    @RequestMapping(value = "quiz/result/{student_id}", method = RequestMethod.GET)
    @ResponseBody
    List<QuizStudentResultResponse> getAllQuizResultsForStudent(@PathVariable(name = "student_id") String student_id);


    @RequestMapping(value = "quiz/result/prof/{prof_id}", method = RequestMethod.GET)
    @ResponseBody
    List<QuizStudentResultResponse> getAllQuizResultsByProfessor(@PathVariable(name = "prof_id") String prof_id);


    @RequestMapping(value = "quizes/tograde/{userId}", method = RequestMethod.GET)
    List<QuizToCorrectRequest> getListOfQuizesToCorrect(@PathVariable(name = "userId") String userId);

    @RequestMapping(value = "quizes/tograde/response/{responseid}", method = RequestMethod.GET)
    QuizToCorrectRequest getQuizResponseToGradeById(@PathVariable(name = "responseid") Long responseid);

    @RequestMapping(value = "quizes/tograde", method = RequestMethod.POST)
    void saveQuizResponseEvaluation(@RequestBody QuizToCorrectRequest quizToCorrectRequest);
}
