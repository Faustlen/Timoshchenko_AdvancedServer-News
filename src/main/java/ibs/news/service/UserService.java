package ibs.news.service;

import ibs.news.dto.response.PublicUserView;
import ibs.news.dto.response.common.CustomSuccessResponse;

public interface UserService {

    public CustomSuccessResponse<?> getAllUsersService();

    public CustomSuccessResponse<PublicUserView> getUserInfoService();

    public CustomSuccessResponse<?> getUserInfoByIdService(String id);
}
