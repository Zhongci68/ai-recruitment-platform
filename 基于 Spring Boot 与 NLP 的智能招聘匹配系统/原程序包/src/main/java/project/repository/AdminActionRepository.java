package project.repository;

import project.entity.AdminAction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdminActionRepository extends JpaRepository<AdminAction, Long> {
    List<AdminAction> findTop8ByOrderByCreateTimeDesc();
}
