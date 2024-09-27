package ibs.news.repository;

import ibs.news.entity.NewsEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Set;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"userId", "tags"})
    Page<NewsEntity> findAll(@NonNull Pageable pageable);

    @NonNull
    @EntityGraph(attributePaths = {"userId", "tags"})
    Page<NewsEntity> findByUserIdId(@NonNull Pageable pageable, UUID userId);

    @Query("SELECT DISTINCT n FROM NewsEntity n " +
            "JOIN FETCH n.userId u " +
            "JOIN FETCH n.tags t " +
            "WHERE (:author IS NULL OR u.name LIKE %:author%) " +
            "AND (COALESCE(:tagSet, NULL) IS NULL OR t.title IN :tagSet) " +
            "AND (:keyword IS NULL OR (n.title LIKE %:keyword% OR n.title LIKE %:keyword%))")
    Page<NewsEntity> findNews(Pageable pageable, String author, String keyword, Set<String> tagSet);
}
