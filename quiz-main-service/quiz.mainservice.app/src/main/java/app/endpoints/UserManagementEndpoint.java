package app.endpoints;

import app.client.IUserServiceFeign;
import endpoints.IUserManagementEndpoint;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by achy_ on 6/13/2017.
 */
@RestController
@CrossOrigin
public class UserManagementEndpoint implements IUserManagementEndpoint {

    @Autowired
    private IUserServiceFeign userServiceFeign;

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User logIn(@RequestParam("username") String username, @RequestParam("password") String password) {
        return userServiceFeign.logIn(username, password);
    }

    @Override
    public User createUser(@RequestBody User user) {
        return userServiceFeign.createUser(user);
    }

    @Override
    public User delete(@PathVariable("id") Long userId) {
        return userServiceFeign.delete(userId);
    }

    @Override
    public User update(@PathVariable("id") Long userId, @RequestBody User user) {
        return userServiceFeign.update(userId, user);
    }

    @Override
    public User getUser(@PathVariable("id") Long userId) {
        return userServiceFeign.getUser(userId);
    }

    @Override
    public User getUser(@PathVariable(name = "username") String username) {
        return userServiceFeign.getUser(username);
    }
}
