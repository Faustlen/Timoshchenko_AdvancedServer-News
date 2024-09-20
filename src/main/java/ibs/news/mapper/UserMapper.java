package ibs.news.mapper;

import ibs.news.dto.response.PublicUserView;
import ibs.news.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    PublicUserView toViewDto(UserEntity user);
}
