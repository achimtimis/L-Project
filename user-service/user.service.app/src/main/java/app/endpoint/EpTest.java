package app.endpoint;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by achy_ on 5/28/2017.
 */
@RestController
public class EpTest {
    @RequestMapping(value = "/test")
    public String test(){
        return "test";
    }
}
