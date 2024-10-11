package ibs.news.mapper;

import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.GetNewsOutResponse;
import ibs.news.entity.NewsEntity;
import ibs.news.entity.TagEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "tags", ignore = true)
    NewsEntity toEntity(CreateNewsRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(source = "dto.title", target = "title")
    @Mapping(source = "dto.description", target = "description")
    @Mapping(source = "dto.image", target = "image")
    @Mapping(source = "tags", target = "tags")
    NewsEntity toEntity(CreateNewsRequest dto, Set<TagEntity> tags, @MappingTarget NewsEntity news);

    @IterableMapping(qualifiedByName = "mapNewsEntityToDto")
    List<GetNewsOutResponse> toDto(List<NewsEntity> newsEntities);

    @Named("mapNewsEntityToDto")
    @Mapping(source = "newsEntity.author.id", target = "userId")
    @Mapping(source = "newsEntity.author.name", target = "username")
    GetNewsOutResponse toDto(NewsEntity newsEntity);
}
