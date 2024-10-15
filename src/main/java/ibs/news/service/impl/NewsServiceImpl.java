package ibs.news.service.impl;

import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.GetNewsOutResponse;
import ibs.news.dto.response.common.BaseSuccessResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.dto.response.common.PageableResponse;
import ibs.news.entity.NewsEntity;
import ibs.news.entity.TagEntity;
import ibs.news.entity.UserEntity;
import ibs.news.error.CustomException;
import ibs.news.error.ErrorCodes;
import ibs.news.mapper.NewsMapper;
import ibs.news.repository.NewsRepository;
import ibs.news.service.NewsService;
import ibs.news.service.TagService;
import ibs.news.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepo;

    private final NewsMapper newsMapper;

    private final TagService tagService;

    private final UserService userService;

    @Override
    @Transactional
    public CustomSuccessResponse<UUID> createNewsService(CreateNewsRequest dto) {
        UserEntity userEntity = userService.getAuthorizedUser();

        Set<TagEntity> tags = tagService.createTags(dto.getTags());

        NewsEntity news = newsMapper.toEntity(dto);
        news.setAuthor(userEntity);
        news.setTags(tags);
        news = newsRepo.save(news);

        return new CustomSuccessResponse<>(news.getId());
    }

    @Override
    public CustomSuccessResponse<PageableResponse<GetNewsOutResponse>> getPageableNewsService(
            Integer page, Integer perPage) {
        Page<NewsEntity> pagedNews;
        pagedNews = newsRepo.findAll(PageRequest.of(page, perPage));

        List<GetNewsOutResponse> newsList = newsMapper.toDto(pagedNews.getContent());

        return new CustomSuccessResponse<>(new PageableResponse<>(newsList, newsRepo.count()));
    }

    @Override
    public CustomSuccessResponse<PageableResponse<GetNewsOutResponse>> getUserNewsService(
            String userIdStr, Integer page, Integer perPage) {
        UUID userId = UUID.fromString(userIdStr);

        Page<NewsEntity> pagedNews = newsRepo.findByAuthorId(PageRequest.of(page, perPage), userId);

        List<GetNewsOutResponse> newsList = newsMapper.toDto(pagedNews.getContent());

        return new CustomSuccessResponse<>(new PageableResponse<>(newsList, newsRepo.count()));
    }

    @Override
    public CustomSuccessResponse<PageableResponse<GetNewsOutResponse>> findNewsService(
            Integer page, Integer perPage, Optional<String> author,
            Optional<String> keywords, Optional<Set<String>> tags) {
        Page<NewsEntity> pagedNews;
        pagedNews = newsRepo.findNews(PageRequest.of(page, perPage),
                author, keywords, tags);

        List<GetNewsOutResponse> newsList = newsMapper.toDto(pagedNews.getContent());

        return new CustomSuccessResponse<>(new PageableResponse<>(newsList, newsRepo.count()));
    }

    @Override
    @Transactional
    public BaseSuccessResponse putNewsService(Long id, CreateNewsRequest dto) {
        UserEntity userEntity = userService.getAuthorizedUser();

        NewsEntity news = newsRepo.findByIdAndAuthor(id, userEntity).orElseThrow(() ->
                new CustomException(ErrorCodes.NEWS_NOT_FOUND));

        Set<TagEntity> tags = tagService.createTags(dto.getTags());
        news = newsMapper.toEntity(dto, tags, news);
        newsRepo.save(news);

        return new BaseSuccessResponse();
    }

    @Override
    @Transactional
    public BaseSuccessResponse deleteNewsService(Long id) {
        UserEntity userEntity = userService.getAuthorizedUser();

        NewsEntity news = newsRepo.findByIdAndAuthor(id, userEntity).orElseThrow(() ->
                new CustomException(ErrorCodes.NEWS_NOT_FOUND));
        if (news == null) {
            throw new CustomException(ErrorCodes.NEWS_NOT_FOUND);
        }

        newsRepo.delete(news);

        return new BaseSuccessResponse();
    }
}
