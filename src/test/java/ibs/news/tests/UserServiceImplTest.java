package ibs.news.tests;

import ibs.news.constrants.Constants;
import ibs.news.dto.request.UserNewDataRequest;
import ibs.news.dto.response.PublicUserResponse;
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

public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private PublicUserResponse publicUserResponse;
    private UserEntityDetails userEntityDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = Constants.createUserEntity();
        publicUserResponse = Constants.createPublicUserView();
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
        List<PublicUserResponse> userViews = List.of(publicUserResponse);
        when(userRepository.findAll()).thenReturn(userEntities);
        when(userMapper.toListViewDto(userEntities)).thenReturn(userViews);

        CustomSuccessResponse<List<PublicUserResponse>> response = userService.getAllUsersService();

        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toListViewDto(userEntities);
    }

    @Test
    void getUserInfoServiceShouldReturnUserInfo() {
        when(userMapper.toViewDto(userEntity)).thenReturn(publicUserResponse);

        CustomSuccessResponse<PublicUserResponse> response = userService.getUserInfoService();

        assertNotNull(response.getData());
        assertEquals(userEntity.getEmail(), response.getData().getEmail());
        verify(userMapper, times(1)).toViewDto(userEntity);
    }

    @Test
    void getUserInfoByIdServiceShouldReturnUserInfo() {
        when(userRepository.findById(Constants.USER_UUID)).thenReturn(Optional.of(userEntity));
        when(userMapper.toViewDto(any(UserEntity.class))).thenReturn(publicUserResponse);

        CustomSuccessResponse<PublicUserResponse> response = userService.getUserInfoByIdService(Constants.USER_UUID.toString());

        assertNotNull(response.getData());
        assertEquals(userEntity.getEmail(), response.getData().getEmail());
        verify(userRepository, times(1)).findById(Constants.USER_UUID);
        verify(userMapper, times(1)).toViewDto(userEntity);
    }

    @Test
    void getUserInfoByIdServiceShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(Constants.USER_UUID)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () ->
                userService.getUserInfoByIdService(Constants.USER_UUID.toString()));

        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        verify(userRepository, times(1)).findById(Constants.USER_UUID);
        verify(userMapper, never()).toViewDto(userEntity);
    }

    @Test
    void replaceUserServiceShouldReplaceUser() {
        UserNewDataRequest request = Constants.createUserNewDataRequest();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userEntity));
        when(userMapper.toEntity(any(UserNewDataRequest.class), any(UserEntity.class))).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toViewDto(userEntity)).thenReturn(publicUserResponse);

        CustomSuccessResponse<PublicUserResponse> response = userService.replaceUserService(request);

        assertNotNull(response.getData());
        assertEquals(request.getEmail(), response.getData().getEmail());
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(userRepository, times(1)).save(userEntity);
        verify(userMapper, times(1)).toEntity(request, userEntity);
    }

    @Test
    void replaceUserServiceShouldThrowExceptionWhenUserWithEmailExists() {
        UserNewDataRequest request = Constants.createUserNewDataRequest();

        when(userRepository.findByEmail((request.getEmail())))
                .thenReturn(Optional.of(Constants.createAnotherUserEntity()));

        CustomException exception = assertThrows(CustomException.class, () ->
                userService.replaceUserService(request));

        assertEquals(ErrorCodes.USER_WITH_THIS_EMAIL_ALREADY_EXIST, exception.getErrorCodes());
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(userRepository, never()).save(userEntity);
        verify(userMapper, never()).toEntity(request, userEntity);
    }

    @Test
    void deleteUserServiceShouldDeleteUserAndNews() {
        userService.deleteUserService();

        verify(userRepository, times(1)).deleteById(userEntityDetails.getUserEntity().getId());
    }
}
