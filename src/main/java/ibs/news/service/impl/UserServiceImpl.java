package ibs.news.service.impl;

import ibs.news.dto.response.PublicUserView;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.entity.UserEntity;
import ibs.news.error.CustomException;
import ibs.news.error.ErrorCodes;
import ibs.news.mapper.UserMapper;
import ibs.news.repository.UserRepository;
import ibs.news.security.UserEntityDetails;
import ibs.news.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserMapper userMapper;

    UserRepository userRepo;

    @Override
    public CustomSuccessResponse<List<PublicUserView>> getAllUsersService() {

        List<PublicUserView> users = userMapper.toListViewDto(userRepo.findAll());

        return new CustomSuccessResponse<>(users);
    }

    @Override
    public CustomSuccessResponse<PublicUserView> getUserInfoService() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntityDetails userEntityDetails = (UserEntityDetails) userDetails;
        UserEntity user = userEntityDetails.getUserEntity();

        PublicUserView response = userMapper.toViewDto(user);

        return new CustomSuccessResponse<>(response);
    }

    @Override
    public CustomSuccessResponse<PublicUserView> getUserInfoByIdService(String idStr) {

        UUID id = UUID.fromString(idStr);

        UserEntity user = userRepo.findById(id).orElseThrow(
                () -> new CustomException(ErrorCodes.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));
        PublicUserView response = userMapper.toViewDto(user);

        return new CustomSuccessResponse<>(response);
    }
}
