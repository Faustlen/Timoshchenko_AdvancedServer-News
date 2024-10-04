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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileServiceImplTest implements Constants {

    @InjectMocks
    FileServiceImpl fileService;

    @BeforeEach
    void SetUp() {
        MockitoAnnotations.openMocks(this);

        fileService = new FileServiceImpl();
        fileService.setShelter("/src/test/java/ibs/news/files/");
    }

    @Test
    void uploadFileServiceShouldReturnFileUrlWhenFileIsUploadedSuccessfully() throws IOException {
        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn(FILE);
        doNothing().when(file).transferTo(any(File.class));

        try (MockedStatic<ServletUriComponentsBuilder> mockedBuilder = mockStatic(ServletUriComponentsBuilder.class)) {

            ServletUriComponentsBuilder builderMock = mock(ServletUriComponentsBuilder.class);

            mockedBuilder.when(ServletUriComponentsBuilder::fromCurrentContextPath).thenReturn(builderMock);
            when(builderMock.path("/v1/file/")).thenReturn(builderMock);
            when(builderMock.path(Objects.requireNonNull(file.getOriginalFilename()))).thenReturn(builderMock);
            when(builderMock.toUriString()).thenReturn("http://localhost:8080/v1/file/" + FILE);

            var response = fileService.uploadFileService(file);

            assertEquals("http://localhost:8080/v1/file/" + FILE, response.getData());
            verify(file, times(1)).transferTo(any(File.class));

            File tempFile = new File(System.getProperty("user.dir") + "/src/test/java/ibs/news/files/" + FILE);
            tempFile.delete();
        }
    }

    @Test
    void uploadFileServiceShouldThrowExceptionWhenFileIsEmpty() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, ()->
                fileService.uploadFileService(file));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(ErrorCodes.UNKNOWN, exception.getErrorCodes());
    }

    @Test
    void getFileServiceShouldReturnFileWhenFileExists () {

        ResponseEntity<Resource> responseEntity = fileService.getFileService("Geralt.jpg");

        assertNotNull(responseEntity.getBody());
        assertInstanceOf(UrlResource.class, responseEntity.getBody());
        HttpHeaders headers = responseEntity.getHeaders();
        assertEquals("attachment; filename=\"Geralt.jpg\"", headers.getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals(MediaType.MULTIPART_FORM_DATA, responseEntity.getHeaders().getContentType());
        Resource resource = responseEntity.getBody();
    }

    @Test
    void getFileServiceShouldThrowExceptionWhenFileNotFound () {

        CustomException exception = assertThrows(CustomException.class, () ->
        fileService.getFileService(FILE));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(ErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED, exception.getErrorCodes());
    }
}