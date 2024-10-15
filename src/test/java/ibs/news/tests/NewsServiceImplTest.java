package ibs.news.tests;

import ibs.news.constrants.Constants;
import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.GetNewsOutResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.entity.NewsEntity;
import ibs.news.entity.UserEntity;
import ibs.news.error.CustomException;
import ibs.news.error.ErrorCodes;
import ibs.news.mapper.NewsMapper;
import ibs.news.repository.NewsRepository;
import ibs.news.security.UserEntityDetails;
import ibs.news.service.UserService;
import ibs.news.service.impl.NewsServiceImpl;
import ibs.news.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NewsServiceImplTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private TagServiceImpl tagService;

    @Mock
    private UserService userService;

    @Mock
    private UserEntityDetails userEntityDetails;

    @InjectMocks
    private NewsServiceImpl newsService;

    private CreateNewsRequest createNewsRequest;
    private NewsEntity newsEntity;

    @BeforeEach
    void SetUp() {
        MockitoAnnotations.openMocks(this);

        userEntityDetails = new UserEntityDetails(Constants.createUserEntity());
        createNewsRequest = Constants.createCreateNewsRequest();
        newsEntity = Constants.createNewsEntity();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userEntityDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createNewsServiceShouldReturnNewsId() {
        when(tagService.createTags(anySet())).thenReturn(Set.of(Constants.createTagEntity()));
        when(newsMapper.toEntity(any(CreateNewsRequest.class))).thenReturn(newsEntity);
        when(newsRepository.save(any(NewsEntity.class))).thenReturn(newsEntity);

        CustomSuccessResponse<UUID> response = newsService.createNewsService(createNewsRequest);

        assertEquals(Constants.NEWS_ID, response.getId());
        verify(newsRepository, times(1)).save(newsEntity);
        verify(newsMapper, times(1)).toEntity(createNewsRequest);
    }

    @Test
    void getNewsServiceShouldReturnPagedNews() {
        Page<NewsEntity> pagedNews = new PageImpl<>(List.of(newsEntity));
        when(newsRepository.findAll(any(PageRequest.class))).thenReturn(pagedNews);
        when(newsMapper.toDto(anyList())).thenReturn(List.of(new GetNewsOutResponse()));

        var response = newsService.getPageableNewsService(0, 10).getData();

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(newsRepository, times(1)).findAll(any(PageRequest.class));
        verify(newsMapper, times(1)).toDto(anyList());
    }

    @Test
    void getUserNewsServiceShouldReturnPagedNews() {
        Page<NewsEntity> pagedNews = new PageImpl<>(List.of(newsEntity));
        when(newsRepository.findByAuthorId(any(PageRequest.class), any(UUID.class))).thenReturn(pagedNews);
        when(newsMapper.toDto(anyList())).thenReturn(List.of(new GetNewsOutResponse()));

        var response = newsService.getUserNewsService(Constants.USER_UUID.toString(),
                0, 10).getData();

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(newsRepository, times(1)).findByAuthorId(any(PageRequest.class), any(UUID.class));
        verify(newsMapper, times(1)).toDto(anyList());
    }

    @Test
    void findNewsServiceShouldReturnPagedNews() {
        Page<NewsEntity> pagedNews = new PageImpl<>(List.of(newsEntity));
        when(newsRepository.findNews(any(PageRequest.class), any(), any(), any())).thenReturn(pagedNews);
        when(newsMapper.toDto(anyList())).thenReturn(List.of(new GetNewsOutResponse()));

        var response = newsService.findNewsService(
                        1,
                        10,
                        Optional.of(Constants.NAME),
                        Optional.of(Constants.TITLE),
                        Optional.of(Set.of(Constants.TAG)))
                .getData();

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(newsRepository, times(1)).findNews(
                any(PageRequest.class), any(), any(), any());
        verify(newsMapper, times(1)).toDto(anyList());
    }

    @Test
    void putNewsServiceShouldUpdateNews() {
        when(newsRepository.findByIdAndAuthor(anyLong(), any())).thenReturn(Optional.of(newsEntity));
        when(tagService.createTags(anySet())).thenReturn(Set.of(Constants.createTagEntity()));
        when(newsMapper.toEntity(any(CreateNewsRequest.class), anySet(),
                any(NewsEntity.class))).thenReturn(newsEntity);

        newsService.putNewsService(Constants.NEWS_ID, createNewsRequest);

        verify(newsRepository, times(1)).save(newsEntity);
        verify(newsMapper, never()).toEntity(Constants.createCreateNewsRequest(),
                Set.of(Constants.createTagEntity()), newsEntity);
    }

    @Test
    void putNewsServiceShouldThrowExceptionWhenUserNewsNotFound() {
        when(newsRepository.findByIdAndAuthor(anyLong(), any(UserEntity.class))).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () ->
                newsService.putNewsService(Constants.NEWS_ID, createNewsRequest));

        assertEquals(ErrorCodes.NEWS_NOT_FOUND, exception.getErrorCodes());
        verify(newsRepository, never()).save(newsEntity);
        verify(newsMapper, never()).toEntity(Constants.createCreateNewsRequest(),
                Set.of(Constants.createTagEntity()), newsEntity);
    }

    @Test
    void deleteNewsServiceShouldDeleteNews() {
        when(newsRepository.findByIdAndAuthor(anyLong(), any())).thenReturn(Optional.of(newsEntity));

        newsService.deleteNewsService(Constants.NEWS_ID);

        verify(newsRepository, times(1)).delete(newsEntity);
    }

    @Test
    void deleteNewsServiceShouldThrowExceptionWhenUserNewsNotFound() {
        when(newsRepository.findByIdAndAuthor(anyLong(), any(UserEntity.class))).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () ->
                newsService.deleteNewsService(Constants.NEWS_ID));

        assertEquals(ErrorCodes.NEWS_NOT_FOUND, exception.getErrorCodes());
        verify(newsRepository, never()).delete(newsEntity);
    }
}
