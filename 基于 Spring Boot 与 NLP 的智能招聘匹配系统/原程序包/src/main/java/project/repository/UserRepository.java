package project.repository;

import project.entity.User;
import project.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByAccount(String account);
    boolean existsByAccount(String account);
    List<User> findByRole(UserRole role);
}
