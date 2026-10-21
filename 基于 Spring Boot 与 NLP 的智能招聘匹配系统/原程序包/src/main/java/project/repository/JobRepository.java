package project.repository;

import project.entity.Job;
import project.entity.JobCategory;
import project.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long>
{
    List<Job> findByCategory(JobCategory category);
    List<Job> findByPublisher(User publisher);
    List<Job> findTop10ByOrderByViewCountDesc();
    List<Job> findTop10ByOrderByCreateTimeDesc();
    boolean existsByTitle(String title);

    @Query("""
        select j from Job j
        where lower(j.title) like lower(concat('%', :keyword, '%'))
           or lower(j.description) like lower(concat('%', :keyword, '%'))
           or lower(j.requirementsText) like lower(concat('%', :keyword, '%'))
           or lower(j.location) like lower(concat('%', :keyword, '%'))
           or lower(j.education) like lower(concat('%', :keyword, '%'))
        """)
    List<Job> search(String keyword);

    @Query("select j from Job j order by j.viewCount desc, j.ratingAverage desc")
    List<Job> rank(Pageable pageable);
}
