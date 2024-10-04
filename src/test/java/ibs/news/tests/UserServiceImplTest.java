package ibs.news.tests;

import ibs.news.constrants.Constants;
import ibs.news.dto.request.UserNewDataRequest;
import ibs.news.dto.response.PublicUserView;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.entity.UserEntity;
import ibs.news.error.CustomException;
import ibs.news.error.ErrorCodes;
import ibs.news.mapper.UserMapper;
import ibs.news.repository.NewsRepository;
import ibs.news.repository.UserRepository;
import ibs.news.security.UserEntityDetails;
import ibs.news.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest implements Constants {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private UserServiceImpl userService;

    UserEntity userEntity;
    PublicUserView publicUserView;
    UserEntityDetails userEntityDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = createUserEntity();
        publicUserView = createPublicUserView();
        userEntityDetails = new UserEntityDetails(userEntity);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userEntityDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getAllUsersServiceShouldReturnListOfUsers() {
        List<UserEntity> userEntities = List.of(userEntity);
        List<PublicUserView> userViews = List.of(publicUserView);
        when(userRepository.findAll()).thenReturn(userEntities);
        when(userMapper.toListViewDto(userEntities)).thenReturn(userViews);

        CustomSuccessResponse<List<PublicUserView>> response = userService.getAllUsersService();

        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toListViewDto(userEntities);
    }

    @Test
    void getUserInfoServiceShouldReturnUserInfo() {
        when(userMapper.toViewDto(userEntity)).thenReturn(publicUserView);

        CustomSuccessResponse<PublicUserView> response = userService.getUserInfoService();

        assertNotNull(response.getData());
        assertEquals(userEntity.getEmail(), response.getData().getEmail());
        verify(userMapper, times(1)).toViewDto(userEntity);
    }

    @Test
    void getUserInfoByIdServiceShouldReturnUserInfo() {
        when(userRepository.findById(USER_UUID)).thenReturn(Optional.of(userEntity));
        when(userMapper.toViewDto(any(UserEntity.class))).thenReturn(publicUserView);

        CustomSuccessResponse<PublicUserView> response = userService.getUserInfoByIdService(USER_UUID.toString());

        assertNotNull(response.getData());
        assertEquals(userEntity.getEmail(), response.getData().getEmail());
        verify(userRepository, times(1)).findById(USER_UUID);
        verify(userMapper, times(1)).toViewDto(userEntity);
    }

    @Test
    void getUserInfoByIdServiceShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(USER_UUID)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () ->
                userService.getUserInfoByIdService(USER_UUID.toString()));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        verify(userRepository, times(1)).findById(USER_UUID);
        verify(userMapper, never()).toViewDto(userEntity);
    }

    @Test
    void replaceUserServiceShouldReplaceUser() {
        UserNewDataRequest request = createUserNewDataRequest();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userMapper.toEntity(any(UserNewDataRequest.class), any(UserEntity.class))).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toViewDto(userEntity)).thenReturn(publicUserView);

        CustomSuccessResponse<PublicUserView> response = userService.replaceUserService(request);

        assertNotNull(response.getData());
        assertEquals(request.getEmail(), response.getData().getEmail());
        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, times(1)).save(userEntity);
        verify(userMapper, times(1)).toEntity(request, userEntity);
    }

    @Test
    void replaceUserServiceShouldThrowExceptionWhenUserWithEmailExists() {
        UserNewDataRequest request = createUserNewDataRequest();

        when(userRepository.existsByEmail((request.getEmail()))).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () ->
                userService.replaceUserService(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(ErrorCodes.USER_WITH_THIS_EMAIL_ALREADY_EXIST, exception.getErrorCodes());
        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, never()).save(userEntity);
        verify(userMapper, never()).toEntity(request, userEntity);
    }

    @Test
    void deleteUserServiceShouldDeleteUserAndNews() {
        userService.deleteUserService();

        verify(newsRepository, times(1)).deleteByUserIdId(userEntityDetails.getId());
        verify(userRepository, times(1)).deleteById(userEntityDetails.getId());
    }
}
