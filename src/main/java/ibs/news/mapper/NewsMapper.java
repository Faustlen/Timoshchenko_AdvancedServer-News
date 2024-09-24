package ibs.news.mapper;

import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.entity.NewsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "tags", ignore = true)
    NewsEntity toEntity(CreateNewsRequest dto);
}
