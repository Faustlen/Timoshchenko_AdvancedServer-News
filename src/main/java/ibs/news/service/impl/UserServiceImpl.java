package ibs.news.service.impl;

import ibs.news.dto.request.UserNewDataRequest;
import ibs.news.dto.response.PublicUserView;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.entity.UserEntity;
import ibs.news.error.CustomException;
import ibs.news.error.ErrorCodes;
import ibs.news.mapper.UserMapper;
import ibs.news.repository.NewsRepository;
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

    private final UserMapper userMapper;

    private final UserRepository userRepo;

    private final NewsRepository newsRepo;

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

    @Override
    public CustomSuccessResponse<PublicUserView> replaceUserService(UserNewDataRequest dto) {

        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new CustomException(ErrorCodes.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        var userDetails = (UserEntityDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity user = userDetails.getUserEntity();

        user = userMapper.toEntity(dto, user);
        user = userRepo.save(user);

        return new CustomSuccessResponse<>(userMapper.toViewDto(user));
    }

    @Override
    public void deleteUserService() {

        var userDetails = (UserEntityDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        newsRepo.deleteByUserIdId(userDetails.getId());

        userRepo.deleteById(userDetails.getId());
    }
}
