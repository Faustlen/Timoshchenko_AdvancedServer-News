package ibs.news.service;

import ibs.news.dto.response.common.CustomSuccessResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

public interface FileService {

    CustomSuccessResponse<String> uploadFileService(MultipartFile file);

    File getFileService(String fileNames);
}
