package ibs.news.service;

import ibs.news.dto.response.common.CustomSuccessResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    CustomSuccessResponse<String> uploadFileService(MultipartFile file);

    ResponseEntity<Resource> getFileService(String fileNames);
}
