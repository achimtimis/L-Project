package app.endpoints;

import app.client.IQuizServiceFeign;
import com.netflix.discovery.converters.Auto;
import endpoints.ITestInterface;
import models.QuizRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by achy_ on 6/12/2017.
 */
@RestController
public class TestEndpoint implements ITestInterface {


    @Autowired
    IQuizServiceFeign quizServiceManager;

    @Override
    public QuizRequest test() {
        return quizServiceManager.getQuizById(1l);
    }
}
