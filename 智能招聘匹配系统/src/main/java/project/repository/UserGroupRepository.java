package project.repository;

import project.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long>
{
}
