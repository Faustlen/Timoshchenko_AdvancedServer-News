package ibs.news.controller;

import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.service.FileService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

@RestController
@RequiredArgsConstructor
@RequestMapping("file")
@Validated
public class FileController {
    private final FileService fileService;

    @PostMapping("uploadFile")
    public ResponseEntity<CustomSuccessResponse<String>> uploadFile(
            @RequestParam(value = "file", required = false)
            @NonNull
            @Valid
            MultipartFile file) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(fileService.uploadFileService(file)));
    }

    @GetMapping("{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        File file = fileService.getFileService(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.valueOf(MediaType.MULTIPART_FORM_DATA_VALUE))
                .body(UrlResource.from(file.toURI()));
    }

}
