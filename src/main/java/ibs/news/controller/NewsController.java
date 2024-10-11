package ibs.news.controller;

import ibs.news.constrants.ValidationConstants;
import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.request.NewsRequest;
import ibs.news.dto.response.GetNewsOutResponse;
import ibs.news.dto.response.common.BaseSuccessResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.dto.response.common.PageableResponse;
import ibs.news.service.NewsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/news")
@Validated
public class NewsController {
    private final NewsService newsService;

    @PostMapping
    public CustomSuccessResponse<java.util.UUID> createNews(@RequestBody @Valid CreateNewsRequest dto) {
        return newsService.createNewsService(dto);
    }

    @GetMapping
    public CustomSuccessResponse<PageableResponse<GetNewsOutResponse>> getNews(@Valid NewsRequest newsRequest) {
        return newsService.getPageableNewsService(newsRequest.getPage() - 1, newsRequest.getPerPage());
    }

    @GetMapping("user/{userId}")
    public CustomSuccessResponse<PageableResponse<GetNewsOutResponse>> getUserNews(
            @PathVariable @UUID(message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) String userId,
            @Valid NewsRequest newsRequest) {
        return newsService.getUserNewsService(userId, newsRequest.getPage() - 1, newsRequest.getPerPage());
    }

    @GetMapping("find")
    public CustomSuccessResponse<PageableResponse<GetNewsOutResponse>> findNews(@Valid NewsRequest newsRequest) {
        return newsService.findNewsService(newsRequest);
    }

    @PutMapping("{newsId}")
    public BaseSuccessResponse putNews(
            @PathVariable
            @Positive(message = ValidationConstants.ID_MUST_BE_POSITIVE)
            Long newsId,
            @RequestBody
            @Valid
            CreateNewsRequest dto) {
        return newsService.putNewsService(newsId, dto);
    }

    @DeleteMapping("{newsId}")
    public BaseSuccessResponse deleteNews(
            @PathVariable @Positive(message = ValidationConstants.ID_MUST_BE_POSITIVE) Long newsId) {
        return newsService.deleteNewsService(newsId);
    }
}
