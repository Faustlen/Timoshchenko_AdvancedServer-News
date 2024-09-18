package ibs.news.repository;

import ibs.news.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
}
