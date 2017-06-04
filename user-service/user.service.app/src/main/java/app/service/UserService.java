package app.service;

import app.domain.UserEntity;
import app.domain.UserProfile;
import app.domain.UserRole;
import app.repository.IUserEntityDao;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by achy_ on 5/16/2017.
 */
@Service
public class UserService {

    @Autowired
    private IUserEntityDao userRepository;

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().stream()
                .forEach(userEntity -> users.add(mapUser(userEntity)));
        return users;
    }

    public User logIn(String username, String password) {
        return mapUser(userRepository.findByUsernameAndPassword(username, password));
    }

    private User mapUser(UserEntity userEntity) {
        return new User(userEntity.getId(), userEntity.getUsername(),
                userEntity.getPassword(), userEntity.getRole().toString(), userEntity.getUserProfile().getFistName(),
                userEntity.getUserProfile().getLastname(), userEntity.getUserProfile().getEmail(),
                userEntity.getUserProfile().getDetails());
    }

    public User createUser(User user) {
        return mapUser(userRepository.saveAndFlush(mapUser(user)));
    }

    private UserEntity mapUser(User user) {
        return new UserEntity(user.getId(), user.getUsername(), user.getPassword(),
                UserRole.valueOf(user.getRole()),
                new UserProfile(null, user.getEmail(), user.getFirstName(), user.getLastName(), user.getDetails()));
    }

    public User deleteUser(Long id) {
        UserEntity user = userRepository.findOne(id);
        if (user != null) {
            userRepository.delete(id);
        }
        return mapUser(user);
    }

    public User updateUser(Long id, User user) {
        User response = null;
        UserEntity userEntity = userRepository.findOne(id);
        if (userEntity != null) {
            response = mapUser(userRepository.saveAndFlush(mapUser(user)));
        }
        return response;
    }

    public User getUserById(Long id) {
        return mapUser(userRepository.findOne(id));
    }
}
