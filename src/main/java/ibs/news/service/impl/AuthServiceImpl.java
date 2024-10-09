package ibs.news.service.impl;

import ibs.news.dto.request.AuthUserRequest;
import ibs.news.dto.request.RegisterUserRequest;
import ibs.news.dto.response.LoginUserResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.entity.UserEntity;
import ibs.news.error.CustomException;
import ibs.news.error.ErrorCodes;
import ibs.news.mapper.AuthMapper;
import ibs.news.repository.UserRepository;
import ibs.news.security.JwtProvider;
import ibs.news.security.UserDetailsServiceImpl;
import ibs.news.security.UserEntityDetails;
import ibs.news.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;

    private final AuthMapper authMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public CustomSuccessResponse<LoginUserResponse> registerService(RegisterUserRequest dto) {
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new CustomException(ErrorCodes.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        UserEntity user = authMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user = userRepo.save(user);

        LoginUserResponse response = authMapper.toDto(user);
        response.setToken(jwtProvider.generateToken(user.getEmail()));

        return new CustomSuccessResponse<>(response);
    }

    @Override
    public CustomSuccessResponse<LoginUserResponse> loginService(AuthUserRequest dto) {

        UserEntityDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());

        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
            throw new CustomException(ErrorCodes.PASSWORD_NOT_VALID, HttpStatus.BAD_REQUEST);
        }

        LoginUserResponse response = authMapper.toDto(userDetails.getUserEntity());
        response.setToken(jwtProvider.generateToken(userDetails.getUsername()));

        return new CustomSuccessResponse<>(response);
    }
}
