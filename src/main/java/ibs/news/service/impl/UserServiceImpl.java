package ibs.news.service.impl;

import ibs.news.dto.request.UserNewDataRequest;
import ibs.news.dto.response.PublicUserResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.entity.UserEntity;
import ibs.news.error.CustomException;
import ibs.news.error.ErrorCodes;
import ibs.news.mapper.UserMapper;
import ibs.news.repository.UserRepository;
import ibs.news.security.UserEntityDetails;
import ibs.news.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    private final UserRepository userRepo;

    @Override
    public CustomSuccessResponse<List<PublicUserResponse>> getAllUsersService() {

        List<PublicUserResponse> users = userMapper.toListViewDto(userRepo.findAll());

        return new CustomSuccessResponse<>(users);
    }

    @Override
    public CustomSuccessResponse<PublicUserResponse> getUserInfoService() {
        UserEntity user = getAuthorizedUser();

        PublicUserResponse response = userMapper.toViewDto(user);

        return new CustomSuccessResponse<>(response);
    }

    @Override
    public CustomSuccessResponse<PublicUserResponse> getUserInfoByIdService(String idStr) {
        UUID id = UUID.fromString(idStr);

        UserEntity user = userRepo.findById(id).orElseThrow(
                () -> new CustomException(ErrorCodes.USER_NOT_FOUND));
        PublicUserResponse response = userMapper.toViewDto(user);

        return new CustomSuccessResponse<>(response);
    }

    @Override
    @Transactional
    public CustomSuccessResponse<PublicUserResponse> replaceUserService(UserNewDataRequest dto) {
        UserEntity user = getAuthorizedUser();

        Optional<UserEntity> existingUser = userRepo.findByEmail(dto.getEmail());
        if (existingUser.isPresent() && !existingUser.get().getEmail().equals(user.getEmail())) {
            throw new CustomException(ErrorCodes.USER_WITH_THIS_EMAIL_ALREADY_EXIST);
        }

        user = userMapper.toEntity(dto, user);
        user = userRepo.save(user);

        return new CustomSuccessResponse<>(userMapper.toViewDto(user));
    }

    @Override
    @Transactional
    public void deleteUserService() {
        userRepo.deleteById(getAuthorizedUser().getId());
    }

    @Override
    public UserEntity getAuthorizedUser() {
        UserEntityDetails userDetails = (UserEntityDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetails.getUserEntity();
    }
}
