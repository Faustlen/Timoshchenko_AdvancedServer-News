package ibs.news.service;

import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.CreateNewsSuccessResponse;
import ibs.news.dto.response.GetNewsOutResponse;
import ibs.news.dto.response.common.BaseSuccessResponse;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.dto.response.common.PageableResponse;
import java.util.List;
import java.util.Set;

public interface NewsService {

    CreateNewsSuccessResponse createNewsService(CreateNewsRequest dto);

    CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNewsService(Integer page, Integer perPage);

    CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getUserNewsService(
            String userIdStr, Integer page, Integer perPage);

    CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> findNewsService(
            Integer page, Integer perPage, String author, String keywords, Set<String> tags);

    BaseSuccessResponse putNewsService(Long id, CreateNewsRequest dto);
}
