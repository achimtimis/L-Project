package app.service;

import app.domain.UserEntity;
import app.domain.UserProfile;
import app.domain.UserRole;
import app.repository.IUserEntityDao;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by achy_ on 5/16/2017.
 */
@Service
@Transactional
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
        if (userRepository.findByUsername(user.getUsername()) == null) {
            return mapUser(userRepository.saveAndFlush(mapUser(user)));
        } else {
            throw new IllegalArgumentException("The given username already exists");
        }

    }

    private UserEntity mapUser(User user) {
        UserProfile userProfile = new UserProfile();
        userProfile.setFistName(user.getFirstName());
        userProfile.setLastname(user.getLastName());
        userProfile.setDetails(user.getDetails());
        userProfile.setEmail(user.getEmail());
        return new UserEntity(user.getId(), user.getUsername(), user.getPassword(),
                UserRole.valueOf(user.getRole()),
                userProfile);
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

    public User getUserByUsername(String username){
        return mapUser(userRepository.findByUsername(username));
    }
}
