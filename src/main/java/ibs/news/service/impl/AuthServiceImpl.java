package ibs.news.service.impl;

import ibs.news.dto.request.LoginUserRequest;
import ibs.news.dto.request.RegisterUserRequest;
import ibs.news.dto.response.LoginUserResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.entity.UserEntity;
import ibs.news.error.CustomException;
import ibs.news.error.ErrorCodes;
import ibs.news.mapper.UserMapper;
import ibs.news.repository.AuthRepository;
import ibs.news.security.JwtProvider;
import ibs.news.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public CustomSuccessResponse<LoginUserResponse> registerService(RegisterUserRequest dto) {
        if (authRepo.existsByEmail(dto.getEmail())) {
            throw new CustomException(ErrorCodes.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        UserEntity user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user = authRepo.save(user);

        LoginUserResponse response = userMapper.toDto(user);
        response.setToken(jwtProvider.generateToken(user.getEmail()));

        return new CustomSuccessResponse<>(response);
    }

    @Override
    public  CustomSuccessResponse<LoginUserResponse> loginService(LoginUserRequest dto) {

        UserEntity user = authRepo.findByEmail(dto.getEmail()).orElseThrow(
                () -> new CustomException(ErrorCodes.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCodes.PASSWORD_NOT_VALID, HttpStatus.BAD_REQUEST);
        }

        LoginUserResponse response = userMapper.toDto(user);
        response.setToken(jwtProvider.generateToken(user.getEmail()));

        return new CustomSuccessResponse<>(response);
    }
}
