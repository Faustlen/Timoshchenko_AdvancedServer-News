package ibs.news.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;

public interface FileService {

    String uploadFileService(MultipartFile file);

    File getFileService(String fileNames);
}
