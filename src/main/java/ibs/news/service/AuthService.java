package ibs.news.service;

import ibs.news.dto.request.LoginUserRequest;
import ibs.news.dto.request.RegisterUserRequest;
import ibs.news.dto.response.LoginUserResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;

public interface AuthService {

    public CustomSuccessResponse<LoginUserResponse> registerService(RegisterUserRequest dto);

    public CustomSuccessResponse<LoginUserResponse> loginService(LoginUserRequest dto);
}
