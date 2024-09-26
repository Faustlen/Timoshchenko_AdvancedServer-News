package ibs.news.service.impl;

import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.CreateNewsSuccessResponse;
import ibs.news.entity.NewsEntity;
import ibs.news.entity.TagEntity;
import ibs.news.mapper.NewsMapper;
import ibs.news.repository.NewsRepository;
import ibs.news.security.UserEntityDetails;
import ibs.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Set;

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

    public void doSomething() {

    }
}
