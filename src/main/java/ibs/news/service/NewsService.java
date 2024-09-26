package ibs.news.service;

import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.CreateNewsSuccessResponse;
import ibs.news.dto.response.GetNewsOutResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.dto.response.common.PageableResponse;

import java.util.List;

public interface NewsService {

    CreateNewsSuccessResponse createNewsService(CreateNewsRequest dto);

    CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNewsService(Integer page, Integer perPage);
}
