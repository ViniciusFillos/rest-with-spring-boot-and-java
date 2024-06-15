package io.github.vinifillos.controllers;

import io.github.vinifillos.model.dto.UploadResponseDto;
import io.github.vinifillos.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "File Endpoint")
@RestController
@RequestMapping("/api/file/v1")
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public UploadResponseDto uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("Storing file to disk!");

        var fileName = fileStorageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/file/v1/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadResponseDto(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadResponseDto> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {

        log.info("Storing files to disk!");

        return Arrays.stream(files)
                .map(this::uploadFile)
                .toList();
    }
}
