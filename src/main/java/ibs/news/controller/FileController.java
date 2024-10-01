package ibs.news.controller;

import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.service.impl.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/file")
@Validated
public class FileController {

    private final FileServiceImpl fileService;

    @PostMapping("/uploadFile")
    public CustomSuccessResponse<String> uploadFile(@RequestParam("file") MultipartFile file) {

        return fileService.uploadFileService(file);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {

        return fileService.getFileService(fileName);
    }

}
