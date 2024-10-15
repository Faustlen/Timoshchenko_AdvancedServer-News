package ibs.news.service;

import ibs.news.dto.request.UserNewDataRequest;
import ibs.news.dto.response.PublicUserResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.entity.UserEntity;

import java.util.List;

public interface UserService {

    CustomSuccessResponse<List<PublicUserResponse>> getAllUsersService();

    CustomSuccessResponse<PublicUserResponse> getUserInfoService();

    CustomSuccessResponse<PublicUserResponse> getUserInfoByIdService(String id);

    CustomSuccessResponse<PublicUserResponse> replaceUserService(UserNewDataRequest dto);

    void deleteUserService();

    UserEntity getAuthorizedUser();
}
