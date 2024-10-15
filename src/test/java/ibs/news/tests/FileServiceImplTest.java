package ibs.news.tests;

import ibs.news.constrants.Constants;
import ibs.news.error.CustomException;
import ibs.news.error.ErrorCodes;
import ibs.news.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class FileServiceImplTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @BeforeEach
    void SetUp() {
        MockitoAnnotations.openMocks(this);

        fileService = new FileServiceImpl();
        fileService.setShelter(Constants.TEMP_SHELTER);
    }

    @Test
    void uploadFileServiceShouldReturnFileUrlWhenFileIsUploadedSuccessfully() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile(
                Constants.FILE, Constants.FILE, "text/plain", ("text").getBytes()
        );

        try (MockedStatic<ServletUriComponentsBuilder> mockedBuilder = mockStatic(ServletUriComponentsBuilder.class)) {
            ServletUriComponentsBuilder builderMock = mock(ServletUriComponentsBuilder.class);

            mockedBuilder.when(ServletUriComponentsBuilder::fromCurrentContextPath).thenReturn(builderMock);
            when(builderMock.path(anyString())).thenReturn(builderMock);
            when(builderMock.path(anyString())).thenReturn(builderMock);
            when(builderMock.toUriString()).thenReturn(Constants.DOWNLOAD_URL + Constants.FILE);

            var response = fileService.uploadFileService(multipartFile);

            assertEquals(Constants.DOWNLOAD_URL + Constants.FILE, response);

            deleteFiles();
        }
    }

    @Test
    void uploadFileServiceShouldThrowExceptionWhenFileIsEmpty() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () ->
                fileService.uploadFileService(file));

        assertEquals(ErrorCodes.UNKNOWN, exception.getErrorCodes());
    }

    @Test
    void getFileServiceShouldReturnFileWhenFileExists() {
        String tempDir = System.getProperty("user.dir") + Constants.TEMP_SHELTER;
        File testFile = new File(tempDir, Constants.FILE);
        File testDir = new File(tempDir);
        if (!testDir.exists()) {
            testDir.mkdirs();
        }
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(Constants.DESCRIPTION);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assert testFile.exists();

        File response = fileService.getFileService(Constants.FILE);

        assertNotNull(response);
        deleteFiles();
    }

    @Test
    void getFileServiceShouldThrowExceptionWhenFileNotFound() {
        CustomException exception = assertThrows(CustomException.class, () ->
                fileService.getFileService(Constants.FILE));

        assertEquals(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED, exception.getErrorCodes());
    }

    private void deleteFiles() {
        Path directoryPath = Path.of(System.getProperty("user.dir") + Constants.TEMP_SHELTER);

        try {
            if (Files.isDirectory(directoryPath)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath)) {
                    for (Path entry : stream) {
                        Files.delete(entry);
                    }
                }
            }
            Files.delete(directoryPath);
        } catch (IOException e) {
        }
    }
}
