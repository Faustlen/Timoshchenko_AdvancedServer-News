package ibs.news.controller;

import ibs.news.dto.request.LoginUserRequest;
import ibs.news.dto.request.RegisterUserRequest;
import ibs.news.dto.response.LoginUserResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.security.UserDetailsService;
import ibs.news.service.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/auth")
@Validated
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/register")
    public CustomSuccessResponse<LoginUserResponse> registerController(@RequestBody @Valid RegisterUserRequest dto) {

        return authService.registerService(dto);
    }

    @PostMapping("/login")
    public CustomSuccessResponse<LoginUserResponse> loginController(@RequestBody @Valid LoginUserRequest dto) {

        return authService.loginService(dto);
    }
}
