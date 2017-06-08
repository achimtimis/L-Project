package app.ep;

import app.client.IUserServiceFeign;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by achy_ on 6/4/2017.
 */
@RestController
@CrossOrigin
public class TestController {

    @Autowired
    private IUserServiceFeign userService;

    @RequestMapping(value = "users/login", method = RequestMethod.GET)
    User logIn(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        return userService.logIn(username, password);
    }


}
