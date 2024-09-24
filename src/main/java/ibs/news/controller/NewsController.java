package ibs.news.controller;

import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.CreateNewsSuccessResponse;
import ibs.news.service.impl.NewsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/news")
@Validated
public class NewsController {

    NewsServiceImpl newsService;

    @PostMapping
    public CreateNewsSuccessResponse createNews(@RequestBody @Valid CreateNewsRequest dto) {

        return newsService.createNewsService(dto);
    }
}
