package ibs.news.service;

import ibs.news.dto.request.UserNewDataRequest;
import ibs.news.dto.response.PublicUserResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.entity.UserEntity;
import ibs.news.security.UserEntityDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public interface UserService {

    CustomSuccessResponse<List<PublicUserResponse>> getAllUsersService();

    CustomSuccessResponse<PublicUserResponse> getUserInfoService();

    CustomSuccessResponse<PublicUserResponse> getUserInfoByIdService(String id);

    CustomSuccessResponse<PublicUserResponse> replaceUserService(UserNewDataRequest dto);

    void deleteUserService();

    default UserEntity getAuthorizedUser() {
        var userDetails = (UserEntityDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUserEntity();
    }
}
