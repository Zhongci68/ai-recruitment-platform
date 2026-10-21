package project.repository;

import project.entity.Comment;
import project.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByJobOrderByCreateTimeDesc(Job job);
}
