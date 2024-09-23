package ibs.news.service;

import ibs.news.dto.request.AuthUserRequest;
import ibs.news.dto.request.RegisterUserRequest;
import ibs.news.dto.response.LoginUserResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;

public interface AuthService {

    CustomSuccessResponse<LoginUserResponse> registerService(RegisterUserRequest dto);

    CustomSuccessResponse<LoginUserResponse> loginService(AuthUserRequest dto);
}
