package ibs.news.tests;

import ibs.news.constrants.Constants;
import ibs.news.dto.request.AuthUserRequest;
import ibs.news.dto.request.RegisterUserRequest;
import ibs.news.dto.response.LoginUserResponse;
import ibs.news.entity.UserEntity;
import ibs.news.error.CustomException;
import ibs.news.error.ErrorCodes;
import ibs.news.mapper.AuthMapper;
import ibs.news.repository.UserRepository;
import ibs.news.security.JwtProvider;
import ibs.news.security.UserDetailsServiceImpl;
import ibs.news.security.UserEntityDetails;
import ibs.news.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceImplTest implements Constants {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private AuthMapper authMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    UserEntity userEntity;
    RegisterUserRequest registerUserRequest;
    AuthUserRequest authUserRequest;
    LoginUserResponse loginUserResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = createUserEntity();
        registerUserRequest =createRegisterUserRequest();
        authUserRequest = createAuthUserRequest();
        loginUserResponse = creteLoginUserResponse();
    }

    @Test
    void registerServiceShouldRegisterNewUser() {

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);
        when(jwtProvider.generateToken(any())).thenReturn(TOKEN);
        when(authMapper.toEntity(any(RegisterUserRequest.class))).thenReturn(userEntity);
        when(authMapper.toDto(any(UserEntity.class))).thenReturn(loginUserResponse);

        LoginUserResponse response = authService.registerService(registerUserRequest).getData();

        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(TOKEN, response.getToken());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void registerServiceShouldThrowExceptionWhenUserAlreadyExists() {

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () ->
                authService.registerService(registerUserRequest));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(ErrorCodes.USER_ALREADY_EXISTS, exception.getErrorCodes());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void loginServiceShouldReturnTokenWhenCredentialsAreValid() {

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(new UserEntityDetails(userEntity));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtProvider.generateToken(any())).thenReturn(TOKEN);
        when(authMapper.toDto(any(UserEntity.class))).thenReturn(loginUserResponse);

        LoginUserResponse response = authService.loginService(authUserRequest).getData();

        assertEquals(TOKEN, response.getToken());
        verify(jwtProvider, times(1)).generateToken(any());
    }

    @Test
    void loginServiceShouldThrowExceptionWhenCredentialsAreInvalid() {

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(new UserEntityDetails(userEntity));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> authService.loginService(authUserRequest));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(ErrorCodes.PASSWORD_NOT_VALID, exception.getErrorCodes());
    }
}