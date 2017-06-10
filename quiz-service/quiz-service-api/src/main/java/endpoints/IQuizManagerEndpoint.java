package endpoints;

import models.QuizCreationRequest;
import models.QuizRequest;
import models.QuizResponseRequest;
import models.questions.QuestionCreatedWithAnswer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
}
