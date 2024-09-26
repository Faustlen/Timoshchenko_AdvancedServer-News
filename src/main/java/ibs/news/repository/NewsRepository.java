package ibs.news.repository;

import ibs.news.entity.NewsEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"userId", "tags"})
    Page<NewsEntity> findAll(@NonNull Pageable pageable);

}
