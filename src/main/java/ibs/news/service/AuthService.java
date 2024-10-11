package ibs.news.service;

import ibs.news.dto.request.AuthUserRequest;
import ibs.news.dto.request.RegisterUserRequest;
import ibs.news.dto.response.LoginUserResponse;

public interface AuthService {

    LoginUserResponse registerService(RegisterUserRequest dto);

    LoginUserResponse loginService(AuthUserRequest dto);
}
