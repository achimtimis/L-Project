package app.endpoint;


import app.service.UserService;
import endpoints.IUserServiceEndpoint;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
public class UserController implements IUserServiceEndpoint {


    @Autowired
    private UserService userService;

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAllUsers() {

        List<User> users = userService.getAllUsers();
        return users;
    }

    @Override
    public User logIn(String username, String password) {
        return userService.logIn(username, password);
    }

    @Override
    public User createUser(@RequestBody User user) {
        User result = null;
        try {
            result =  userService.createUser(user);
        } catch (Exception e) {
            //
        }
        return result;
    }

    @Override
    public User delete(Long id) {
        User existingUser = userService.deleteUser(id);
        return existingUser;
    }

    @Override
    public User update(Long id, User user) {
        return userService.updateUser(id, user);

    }

    @Override
    public User getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @Override
    public User getUser(@PathVariable(name = "username") String username) {
        return userService.getUserByUsername(username);
    }


}
