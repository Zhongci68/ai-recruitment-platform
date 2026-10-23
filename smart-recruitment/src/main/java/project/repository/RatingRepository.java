package project.repository;

import project.entity.Job;
import project.entity.Rating;
import project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long>
{
    List<Rating> findByUserOrderByCreateTimeDesc(User user);
    List<Rating> findByJob(Job job);
    Optional<Rating> findByJobAndUser(Job job, User user);
}
