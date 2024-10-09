package ibs.news.controller;

import ibs.news.constrants.ValidationConstants;
import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.CreateNewsSuccessResponse;
import ibs.news.dto.response.GetNewsOutResponse;
import ibs.news.dto.response.common.BaseSuccessResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.dto.response.common.PageableResponse;
import ibs.news.service.impl.NewsServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

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

    @GetMapping("/user/{userId}")
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getUserNews(
            @PathVariable @UUID(message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) String userId,
            @Valid NewsRequest newsRequest) {

        return newsService.getUserNewsService(userId, newsRequest.getPage() - 1, newsRequest.getPerPage());
    }

    @GetMapping("/find")
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> findNews(@Valid NewsRequest newsRequest) {

        return newsService.findNewsService(newsRequest.getPage() - 1, newsRequest.getPerPage(),
                newsRequest.getAuthor(), newsRequest.getKeywords(), newsRequest.getTags());
    }

    @PutMapping("/{newsId}")
    public BaseSuccessResponse putNews(
            @PathVariable @Positive(message = ValidationConstants.ID_MUST_BE_POSITIVE) Long newsId,
            @RequestBody @Valid CreateNewsRequest dto) {

        return newsService.putNewsService(newsId, dto);
    }

    @DeleteMapping("/{newsId}")
    public BaseSuccessResponse deleteNews(
            @PathVariable @Positive(message = ValidationConstants.ID_MUST_BE_POSITIVE) Long newsId) {

        return newsService.deleteNewsService(newsId);
    }

    @Data
    public static class NewsRequest {

        @NotNull(message = ValidationConstants.PARAM_PAGE_NOT_NULL)
        @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1)
        private Integer page;

        @NotNull(message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL)
        @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100)
        @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
        private Integer perPage;

        private String author;

        private String keywords;

        private Set<String> tags;
    }
}
