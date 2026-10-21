package project.repository;

import project.entity.ApplicationRecord;
import project.entity.Job;
import project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRecordRepository extends JpaRepository<ApplicationRecord, Long>
{
    List<ApplicationRecord> findByUserOrderByApplyTimeDesc(User user);
    List<ApplicationRecord> findByJob(Job job);
    boolean existsByJobAndUser(Job job, User user);
}
