package endpoints;

import models.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
public interface IUserServiceEndpoint {
    @RequestMapping(method = RequestMethod.GET)
    List<User> getAllUsers();

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    User logIn(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password);

    @RequestMapping(method = RequestMethod.POST)
    User createUser(@RequestBody User user);

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    User delete(@PathVariable Long id);


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    User update(@PathVariable Long id, @RequestBody User user);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    User getUser(@PathVariable("id") Long id);

}


