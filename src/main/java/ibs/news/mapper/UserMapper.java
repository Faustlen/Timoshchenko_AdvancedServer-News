package ibs.news.mapper;

import ibs.news.dto.response.PublicUserView;
import ibs.news.entity.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    PublicUserView toViewDto(UserEntity user);

    List<PublicUserView> toListViewDto(List<UserEntity> users);
}
