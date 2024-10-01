package ibs.news.service.impl;

import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.error.CustomException;
import ibs.news.error.ErrorCodes;
import ibs.news.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${files.shelter.path}")
    private String shelter;

    @Override
    public CustomSuccessResponse<String> uploadFileService(MultipartFile file) {

        if (file.isEmpty()) {
            throw new CustomException(ErrorCodes.UNKNOWN, HttpStatus.BAD_REQUEST);
        }

        try {
            Path fileStorageLocation = Path.of(System.getProperty("user.dir") + shelter);
            File destinationFile = new File(fileStorageLocation + "/" + file.getOriginalFilename());
            System.out.println(destinationFile);
            if (!destinationFile.exists()) {
                destinationFile.mkdirs();
            }

            file.transferTo(destinationFile);
            String fileUrl = "http://localhost:8080/v1/file/" + file.getOriginalFilename();
            return new CustomSuccessResponse<>(fileUrl);
        } catch (IOException e) {
            throw new CustomException(ErrorCodes.UNKNOWN, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Resource> getFileService(String fileName) {
        try {
            Path fileStorageLocation = Path.of(System.getProperty("user.dir") + shelter);
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .contentType(MediaType.valueOf(MediaType.MULTIPART_FORM_DATA_VALUE))
                        .body(resource);
            } else {
                throw new CustomException(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED, HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            throw new CustomException(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED, HttpStatus.BAD_REQUEST);
        }
    }
}
