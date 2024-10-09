package ibs.news.mapper;

import ibs.news.dto.request.UserNewDataRequest;
import ibs.news.dto.response.PublicUserView;
import ibs.news.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    PublicUserView toViewDto(UserEntity user);

    List<PublicUserView> toListViewDto(List<UserEntity> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserEntity toEntity(UserNewDataRequest dto, @MappingTarget UserEntity user);
}
