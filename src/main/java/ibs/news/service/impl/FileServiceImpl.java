package ibs.news.service.impl;

import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.error.CustomException;
import ibs.news.error.ErrorCodes;
import ibs.news.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Setter
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

            if (!destinationFile.exists()) {
                destinationFile.mkdirs();
            }

            file.transferTo(destinationFile);
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/v1/file/")
                    .path(Objects.requireNonNull(file.getOriginalFilename()))
                    .toUriString();
            return new CustomSuccessResponse<>(fileUrl);
        } catch (IOException e) {
            throw new CustomException(ErrorCodes.UNKNOWN, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public File getFileService(String fileName) {
        File file = new File(System.getProperty("user.dir") + shelter, fileName);

        if (!file.exists() || !file.isFile()) {
            throw new CustomException(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED, HttpStatus.BAD_REQUEST);
        }

        return file;
    }
}
