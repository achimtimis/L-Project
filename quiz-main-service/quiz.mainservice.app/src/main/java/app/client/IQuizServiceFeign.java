package app.client;

import endpoints.IQuizManagerEndpoint;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by achy_ on 6/13/2017.
 */
@FeignClient(value = "quiz-service")
public interface IQuizServiceFeign extends IQuizManagerEndpoint{
}
