package ibs.news.controller;

import ibs.news.dto.request.AuthUserRequest;
import ibs.news.dto.request.RegisterUserRequest;
import ibs.news.dto.response.LoginUserResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@Validated
public class AuthController {
    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> registerController(
            @RequestBody @Valid RegisterUserRequest dto) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(authService.registerService(dto)));
    }

    @PostMapping("login")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> loginController(
            @RequestBody @Valid AuthUserRequest dto) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(authService.loginService(dto)));
    }
}
