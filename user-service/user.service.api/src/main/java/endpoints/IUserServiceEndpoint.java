package endpoints;

import models.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IUserServiceEndpoint {
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    List<User> getAllUsers();

    @RequestMapping(value = "/users/login", method = RequestMethod.GET)
    @ResponseBody
    User logIn(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password);

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    User createUser(@RequestBody User user);

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    User delete(@PathVariable(value = "id") Long id);


    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    User update(@PathVariable(value = "id") Long id, @RequestBody User user);

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    User getUser(@PathVariable(value = "id") Long id);

    @RequestMapping(value = "users/{username}", method = RequestMethod.GET)
    @ResponseBody
    User getUser(@PathVariable(name = "username") String username);

}


