package project.repository;

import project.entity.Interview;
import project.entity.ApplicationRecord;
import project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface InterviewRepository extends JpaRepository<Interview, Long>
{
    List<Interview> findByInterviewer(User interviewer);
    List<Interview> findByCandidate(User candidate);
    Optional<Interview> findTopByApplicationOrderByInterviewTimeDesc(ApplicationRecord application);
}
