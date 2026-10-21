package project.repository;

import project.entity.Favorite;
import project.entity.Job;
import project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserOrderByCreateTimeDesc(User user);
    Optional<Favorite> findByJobAndUser(Job job, User user);
    boolean existsByJobAndUser(Job job, User user);
}
