package ibs.news.repository;

import ibs.news.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Set;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

    @Query("SELECT t FROM TagEntity t WHERE t.title IN :tagsList")
    Set<TagEntity> findTags(Set<String> tagsList);
}
