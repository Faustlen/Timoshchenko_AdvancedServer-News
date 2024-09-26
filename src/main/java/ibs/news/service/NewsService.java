package ibs.news.service;

import ibs.news.dto.request.CreateNewsRequest;
import ibs.news.dto.response.CreateNewsSuccessResponse;

public interface NewsService {

    CreateNewsSuccessResponse createNewsService(CreateNewsRequest dto);

}
