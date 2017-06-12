package endpoints;

import models.QuizRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by achy_ on 6/13/2017.
 */
public interface ITestInterface {

    @RequestMapping(name = "/test", method = RequestMethod.GET)
    QuizRequest test();
}
