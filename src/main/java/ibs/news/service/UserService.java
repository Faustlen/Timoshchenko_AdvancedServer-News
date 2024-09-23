package ibs.news.service;

import ibs.news.dto.request.UserNewDataRequest;
import ibs.news.dto.response.PublicUserView;
import ibs.news.dto.response.common.CustomSuccessResponse;

import java.util.List;

public interface UserService {

    CustomSuccessResponse<List<PublicUserView>> getAllUsersService();

    CustomSuccessResponse<PublicUserView> getUserInfoService();

    CustomSuccessResponse<PublicUserView> getUserInfoByIdService(String id);

    CustomSuccessResponse<PublicUserView> replaceUserService(UserNewDataRequest dto);

//    BaseSuccessResponse deleteUserService();
}
