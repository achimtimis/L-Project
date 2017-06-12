package endpoints;

import models.*;
import models.questions.QuestionCreatedWithAnswer;
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


    @RequestMapping(value = "quiz/{id}", method = RequestMethod.GET)
    @ResponseBody
    QuizRequest getQuizById(@PathVariable(value = "id") Long id);


    @RequestMapping(value = "quiz/response", method = RequestMethod.POST)
    @ResponseBody
    QuizResponseRequest saveQuizResponse(@RequestBody QuizResponseRequest quizResponseRequest);

    @RequestMapping(value = "quiz/result/{student_id}/{quiz_id}", method = RequestMethod.GET)
    @ResponseBody
    QuizStudentResultResponse getQuizResultForStudent(@PathVariable(name = "student_id") String student_id,
                                                      @PathVariable(name = "quiz_id") Long quiz_id);

    @RequestMapping(value = "quizes/tograde/{userId}/{quizId}", method = RequestMethod.GET)
    List<QuizToCorrectRequest> getListOfQuizesToCorrect(@PathVariable(name = "userId") String userId,
                                                        @PathVariable(name = "quizId") Long quizId);
}
