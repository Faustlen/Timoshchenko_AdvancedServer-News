package ibs.news.service.impl;

import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.CreateNewsSuccessResponse;
import ibs.news.entity.NewsEntity;
import ibs.news.mapper.NewsMapper;
import ibs.news.repository.NewsRepository;
import ibs.news.security.UserEntityDetails;
import ibs.news.service.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepo;

    private final NewsMapper newsMapper;

    @Override
    public CreateNewsSuccessResponse createNewsService(CreateNewsRequest dto) {

        var userEntityDetails = (UserEntityDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        NewsEntity news = newsMapper.toEntity(dto);
        news.setUserId(userEntityDetails.getUserEntity());

        return null;
    }
}
