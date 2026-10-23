package project.repository;

import project.entity.Resume;
import project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByUser(User user);
}
