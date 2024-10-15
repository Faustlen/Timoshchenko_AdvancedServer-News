package ibs.news.service.impl;

import ibs.news.constrants.FileConstants;
import ibs.news.error.CustomException;
import ibs.news.error.ErrorCodes;
import ibs.news.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Setter
public class FileServiceImpl implements FileService {
    @Value("${files.shelter.path}")
    private String shelter;

    @Override
    @Transactional
    public String uploadFileService(MultipartFile file) {
        if (file.isEmpty()) {
            throw new CustomException(ErrorCodes.UNKNOWN);
        }

        try {
            String uniqueFileName = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null && originalFilename.contains(FileConstants.DOT)) {
                uniqueFileName += FileConstants.DOT + originalFilename.substring(
                        originalFilename.lastIndexOf(FileConstants.DOT) + 1);
            }

            Path fileStorageLocation = Path.of(System.getProperty("user.dir") + shelter);
            File destinationFile = new File(fileStorageLocation + FileConstants.SLASH + uniqueFileName);

            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            file.transferTo(destinationFile);
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(FileConstants.URI)
                    .path(uniqueFileName)
                    .toUriString();
        } catch (IOException e) {
            throw new CustomException(ErrorCodes.UNKNOWN);
        }
    }

    @Override
    public File getFileService(String fileName) {
        File file = new File(System.getProperty("user.dir") + shelter, fileName);

        if (!file.exists() || !file.isFile()) {
            throw new CustomException(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED);
        }

        return file;
    }
}
