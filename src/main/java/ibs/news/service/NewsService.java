package ibs.news.service;

import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.request.NewsRequest;
import ibs.news.dto.response.GetNewsOutResponse;
import ibs.news.dto.response.common.BaseSuccessResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.dto.response.common.PageableResponse;
import java.util.UUID;

public interface NewsService {

    CustomSuccessResponse<UUID> createNewsService(CreateNewsRequest dto);

    CustomSuccessResponse<PageableResponse<GetNewsOutResponse>> getPageableNewsService(Integer page, Integer perPage);

    CustomSuccessResponse<PageableResponse<GetNewsOutResponse>> getUserNewsService(
            String userIdStr, Integer page, Integer perPage);

    CustomSuccessResponse<PageableResponse<GetNewsOutResponse>> findNewsService(NewsRequest newsRequest);

    BaseSuccessResponse putNewsService(Long id, CreateNewsRequest dto);

    BaseSuccessResponse deleteNewsService(Long id);
}
