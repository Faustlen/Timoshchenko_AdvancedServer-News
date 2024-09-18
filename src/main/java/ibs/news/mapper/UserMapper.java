package ibs.news.mapper;

import ibs.news.dto.request.RegisterUserRequest;
import ibs.news.dto.response.LoginUserResponse;
import ibs.news.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserEntity toEntity(RegisterUserRequest dto);

    @Mapping(target = "token", ignore = true)
    LoginUserResponse toDto(UserEntity user);
}
