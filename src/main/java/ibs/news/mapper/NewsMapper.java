package ibs.news.mapper;

import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.GetNewsOutResponse;
import ibs.news.entity.NewsEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "tags", ignore = true)
    NewsEntity toEntity(CreateNewsRequest dto);

    @IterableMapping(qualifiedByName = "mapNewsEntityToDto")
    List<GetNewsOutResponse> toDto(List<NewsEntity> newsEntities);

    @Named("mapNewsEntityToDto")
    @Mapping(source = "newsEntity.userId.id", target = "userId")
    @Mapping(source = "newsEntity.userId.name", target = "username")
    GetNewsOutResponse toDto(NewsEntity newsEntity);
}
