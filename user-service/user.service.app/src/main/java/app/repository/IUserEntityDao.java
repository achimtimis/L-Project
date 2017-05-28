package app.repository;

import app.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserEntityDao extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsernameAndPassword(String username, String password);
}
