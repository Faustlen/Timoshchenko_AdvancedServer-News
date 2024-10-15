package ibs.news.controller;

import ibs.news.constrants.ValidationConstants;
import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.GetNewsOutResponse;
import ibs.news.dto.response.common.BaseSuccessResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.dto.response.common.PageableResponse;
import ibs.news.service.NewsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("news")
@Validated
public class NewsController {
    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<CustomSuccessResponse<Long>> createNews(@RequestBody @Valid CreateNewsRequest dto) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(newsService.createNewsService(dto).getId()));
    }

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<PageableResponse<GetNewsOutResponse>>> getNews(
            @NotNull(message = ValidationConstants.PARAM_PAGE_NOT_NULL)
            @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1)
            Integer page,
            @NotNull(message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100)
            @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            Integer perPage) {
        return ResponseEntity.ok(newsService.getPageableNewsService(page - 1, perPage));
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<CustomSuccessResponse<PageableResponse<GetNewsOutResponse>>> getUserNews(
            @PathVariable @UUID(message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) String userId,
            @NotNull(message = ValidationConstants.PARAM_PAGE_NOT_NULL)
            @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1)
            Integer page,
            @NotNull(message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100)
            @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            Integer perPage) {
        return ResponseEntity.ok(newsService.getUserNewsService(userId, page - 1, perPage));
    }

    @GetMapping("find")
    public ResponseEntity<CustomSuccessResponse<PageableResponse<GetNewsOutResponse>>> findNews(
            @NotNull(message = ValidationConstants.PARAM_PAGE_NOT_NULL)
            @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1)
            Integer page,
            @NotNull(message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100)
            @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            Integer perPage,
            Optional<String> author,
            Optional<String> keywords,
            Optional<Set<String>> tags) {
        return ResponseEntity.ok(newsService.findNewsService(page - 1, perPage, author, keywords, tags));
    }

    @PutMapping("{newsId}")
    public ResponseEntity<BaseSuccessResponse> putNews(
            @PathVariable
            @Positive(message = ValidationConstants.ID_MUST_BE_POSITIVE)
            Long newsId,
            @RequestBody
            @Valid
            CreateNewsRequest dto) {
        return ResponseEntity.ok(newsService.putNewsService(newsId, dto));
    }

    @DeleteMapping("{newsId}")
    public ResponseEntity<BaseSuccessResponse> deleteNews(
            @PathVariable @Positive(message = ValidationConstants.ID_MUST_BE_POSITIVE) Long newsId) {
        return ResponseEntity.ok(newsService.deleteNewsService(newsId));
    }
}
