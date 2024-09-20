package ibs.news.service.impl;

import ibs.news.dto.response.PublicUserView;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.entity.UserEntity;
import ibs.news.security.UserEntityDetails;
import ibs.news.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public CustomSuccessResponse<?> getAllUsersService() {
        return null;
    }

    @Override
    public CustomSuccessResponse<PublicUserView> getUserInfoService() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntityDetails userEntityDetails = (UserEntityDetails) userDetails;
        UserEntity user = userEntityDetails.getUserEntity();

        PublicUserView response = new PublicUserView(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getAvatar()
        );

        return new CustomSuccessResponse<>(response);
    }

    @Override
    public CustomSuccessResponse<?> getUserInfoByIdService(String id) {

        UUID.fromString(id);

        return null;
    }
}
