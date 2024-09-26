package ibs.news.dto.response;

import ibs.news.entity.TagEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class GetNewsOutResponse {

    private Long id;

    private String title;

    private String description;

    private String image;

    private Set<TagEntity> tags;

    private UUID userId;

    private String username;
}
