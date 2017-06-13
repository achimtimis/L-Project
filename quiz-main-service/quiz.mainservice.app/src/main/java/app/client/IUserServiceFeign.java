package app.client;

import endpoints.IUserServiceEndpoint;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by achy_ on 6/13/2017.
 */
@FeignClient(value = "user-service")
public interface IUserServiceFeign extends IUserServiceEndpoint {

}
