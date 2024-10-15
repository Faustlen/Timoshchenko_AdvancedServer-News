package ibs.news.repository;

import ibs.news.entity.NewsEntity;
import ibs.news.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

    @EntityGraph(attributePaths = {"author", "tags"})
    Page<NewsEntity> findAll(Pageable pageable);

    @Query("SELECT DISTINCT n FROM NewsEntity n " +
            "JOIN FETCH n.author u " +
            "JOIN FETCH n.tags t " +
            "WHERE n.id = :newsId AND n.author = :user")
    Optional<NewsEntity> findByIdAndAuthor(Long newsId, UserEntity user);

    @EntityGraph(attributePaths = {"userId", "tags"})
    Page<NewsEntity> findByAuthorId(Pageable pageable, UUID userId);

    @Query("SELECT DISTINCT n FROM NewsEntity n " +
            "JOIN FETCH n.author u " +
            "JOIN FETCH n.tags t " +
            "WHERE (:author IS NULL OR u.name LIKE %:author%) " +
            "AND (COALESCE(:tagSet, NULL) IS NULL OR t.title IN :tagSet) " +
            "AND (:keyword IS NULL OR (n.title LIKE %:keyword% OR n.description LIKE %:keyword%))")
    Page<NewsEntity> findNews(Pageable pageable, Optional<String> author,
                              Optional<String> keyword, Optional<Set<String>> tagSet);

    @Transactional
    void deleteByAuthorId(UUID userId);
}
