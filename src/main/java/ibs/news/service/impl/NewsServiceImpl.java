package ibs.news.service.impl;

import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.CreateNewsSuccessResponse;
import ibs.news.dto.response.GetNewsOutResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.dto.response.common.PageableResponse;
import ibs.news.entity.NewsEntity;
import ibs.news.entity.TagEntity;
import ibs.news.mapper.NewsMapper;
import ibs.news.repository.NewsRepository;
import ibs.news.security.UserEntityDetails;
import ibs.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepo;

    private final NewsMapper newsMapper;

    private final TagServiceImpl tagService;

    @Override
    public CreateNewsSuccessResponse createNewsService(CreateNewsRequest dto) {

        var userEntityDetails = (UserEntityDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Set<TagEntity> tags = tagService.createTags(dto.getTags());

        NewsEntity news = newsMapper.toEntity(dto);
        news.setUserId(userEntityDetails.getUserEntity());
        news.setTags(tags);
        news = newsRepo.save(news);

        return new CreateNewsSuccessResponse(news.getId());
    }

    @Override
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNewsService(
            Integer page, Integer perPage) {

        Page<NewsEntity> pagedNews;
        pagedNews = newsRepo.findAll(PageRequest.of(page, perPage, Sort.by("id").descending()));

        List<GetNewsOutResponse> newsList = newsMapper.toDto(pagedNews.getContent());

        var response = new PageableResponse<>(newsList, newsRepo.count());

        return new CustomSuccessResponse<>(response);
    }

    @Override
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getUserNewsService(
            String userIdStr, Integer page, Integer perPage) {

        UUID userId = UUID.fromString(userIdStr);

        Page<NewsEntity> pagedNews;
        pagedNews = newsRepo.findByUserIdId(PageRequest.of(page, perPage, Sort.by("id").descending()), userId);

        List<GetNewsOutResponse> newsList = newsMapper.toDto(pagedNews.getContent());

        var response = new PageableResponse<>(newsList, newsRepo.count());

        return new CustomSuccessResponse<>(response);
    }

    @Override
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> findNewsService(
            Integer page, Integer perPage, String author, String keywords, Set<String> tags) {

        Page<NewsEntity> pagedNews;
        pagedNews = newsRepo.findNews(PageRequest.of(page, perPage, Sort.by("id").descending()),
                author, keywords, tags);

        List<GetNewsOutResponse> newsList = newsMapper.toDto(pagedNews.getContent());

        var response = new PageableResponse<>(newsList, newsRepo.count());

        return new CustomSuccessResponse<>(response);
    }
}
