package ibs.news.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@Table(name = "news")
public class NewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userId;

    private String title;

    private String description;

    private String image;

    @ManyToMany
    @JoinTable(
            name = "news_tags",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "tags_id")
    )
    private Set<TagEntity> tags;
}
