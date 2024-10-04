package ibs.news.tests;

import ibs.news.constrants.Constants;
import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.CreateNewsSuccessResponse;
import ibs.news.dto.response.GetNewsOutResponse;
import ibs.news.dto.response.common.PageableResponse;
import ibs.news.entity.NewsEntity;
import ibs.news.entity.UserEntity;
import ibs.news.error.CustomException;
import ibs.news.error.ErrorCodes;
import ibs.news.mapper.NewsMapper;
import ibs.news.repository.NewsRepository;
import ibs.news.security.UserEntityDetails;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NewsServiceImplTest implements Constants {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private TagServiceImpl tagService;

    @Mock
    private UserEntityDetails userEntityDetails;

    @InjectMocks
    private NewsServiceImpl newsService;

    CreateNewsRequest createNewsRequest;
    NewsEntity newsEntity;

    @BeforeEach
    void SetUp() {
        MockitoAnnotations.openMocks(this);

        userEntityDetails = new UserEntityDetails(createUserEntity());
        createNewsRequest = createCreateNewsRequest();
        newsEntity = createNewsEntity();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userEntityDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createNewsServiceShouldReturnNewsId() {
        when(tagService.createTags(anySet())).thenReturn(Set.of(createTagEntity()));
        when(newsMapper.toEntity(any(CreateNewsRequest.class))).thenReturn(newsEntity);
        when(newsRepository.save(any(NewsEntity.class))).thenReturn(newsEntity);

        CreateNewsSuccessResponse response = newsService.createNewsService(createNewsRequest);

        assertEquals(NEWS_ID, response.getId());
        verify(newsRepository, times(1)).save(newsEntity);
        verify(newsMapper, times(1)).toEntity(createNewsRequest);
    }

    @Test
    void getNewsServiceShouldReturnPagedNews() {
        Page<NewsEntity> pagedNews = new PageImpl<>(List.of(newsEntity));
        when(newsRepository.findAll(any(PageRequest.class))).thenReturn(pagedNews);
        when(newsMapper.toDto(anyList())).thenReturn(List.of(new GetNewsOutResponse()));

        PageableResponse<List<GetNewsOutResponse>> response = newsService.getNewsService(0, 10).getData();

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(newsRepository, times(1)).findAll(any(PageRequest.class));
        verify(newsMapper, times(1)).toDto(anyList());
    }

    @Test
    void getUserNewsServiceShouldReturnPagedNews() {
        Page<NewsEntity> pagedNews = new PageImpl<>(List.of(newsEntity));
        when(newsRepository.findByUserIdId(any(PageRequest.class), any(UUID.class))).thenReturn(pagedNews);
        when(newsMapper.toDto(anyList())).thenReturn(List.of(new GetNewsOutResponse()));

        PageableResponse<List<GetNewsOutResponse>> response = newsService.getUserNewsService(USER_UUID.toString(),
                0, 10).getData();

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(newsRepository, times(1)).findByUserIdId(any(PageRequest.class), any(UUID.class));
        verify(newsMapper, times(1)).toDto(anyList());
    }

    @Test
    void findNewsServiceShouldReturnPagedNews() {
        Page<NewsEntity> pagedNews = new PageImpl<>(List.of(newsEntity));
        when(newsRepository.findNews(any(PageRequest.class), anyString(), anyString(), anySet())).thenReturn(pagedNews);
        when(newsMapper.toDto(anyList())).thenReturn(List.of(new GetNewsOutResponse()));

        PageableResponse<List<GetNewsOutResponse>> response = newsService.findNewsService(
                0, 10, NAME, TITLE, Set.of(TAG)).getData();

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(newsRepository, times(1)).findNews(
                any(PageRequest.class),anyString(), anyString(), anySet());
        verify(newsMapper, times(1)).toDto(anyList());
    }

    @Test
    void putNewsServiceShouldUpdateNews() {
        when(newsRepository.findByIdAndAuthor(anyLong(), any(UserEntity.class))).thenReturn(newsEntity);
        when(tagService.createTags(anySet())).thenReturn(Set.of(createTagEntity()));
        when(newsMapper.toEntity(any(CreateNewsRequest.class), any(NewsEntity.class))).thenReturn(newsEntity);

        newsService.putNewsService(NEWS_ID, createNewsRequest);

        verify(newsRepository, times(1)).save(newsEntity);
        verify(newsMapper, times(1)).toEntity(createNewsRequest, newsEntity);
    }

    @Test
    void putNewsServiceShouldThrowExceptionWhenUserNewsNotFound() {
        when(newsRepository.findByIdAndAuthor(anyLong(), any(UserEntity.class))).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () ->
                newsService.putNewsService(NEWS_ID, createNewsRequest));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(ErrorCodes.NEWS_NOT_FOUND, exception.getErrorCodes());
        verify(newsRepository, never()).save(newsEntity);
        verify(newsMapper, never()).toEntity(createNewsRequest, newsEntity);
    }

    @Test
    void deleteNewsServiceShouldDeleteNews() {
        when(newsRepository.findByIdAndAuthor(anyLong(), any(UserEntity.class))).thenReturn(newsEntity);

        newsService.deleteNewsService(NEWS_ID);

        verify(newsRepository, times(1)).delete(newsEntity);
    }

    @Test
    void deleteNewsServiceShouldThrowExceptionWhenUserNewsNotFound() {
        when(newsRepository.findByIdAndAuthor(anyLong(), any(UserEntity.class))).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () ->
                newsService.deleteNewsService(NEWS_ID));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(ErrorCodes.NEWS_NOT_FOUND, exception.getErrorCodes());
        verify(newsRepository, never()).delete(newsEntity);
    }
}
