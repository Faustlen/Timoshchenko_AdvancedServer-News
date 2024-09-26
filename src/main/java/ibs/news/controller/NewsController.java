package ibs.news.controller;

import ibs.news.constrants.ValidationConstants;
import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.CreateNewsSuccessResponse;
import ibs.news.dto.response.GetNewsOutResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.dto.response.common.PageableResponse;
import ibs.news.service.impl.NewsServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/news")
@Validated
public class NewsController {

    private final NewsServiceImpl newsService;

    @PostMapping
    public CreateNewsSuccessResponse createNews(@RequestBody @Valid CreateNewsRequest dto) {

        return newsService.createNewsService(dto);
    }

    @GetMapping
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNews(@Valid NewsRequest newsRequest) {

        return newsService.getNewsService(newsRequest.getPage() - 1, newsRequest.getPerPage());
    }

    @Data
    public static class NewsRequest {

        @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1)
        private Integer page;

        @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100)
        @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
        private Integer perPage;
    }
}
