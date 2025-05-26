package com.capstone.ads.controller;

import com.capstone.ads.dto.file.FileData;
import com.capstone.ads.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
@Slf4j
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
        List<String> keys = s3Service.uploadFiles(files);
        return new ResponseEntity<>(keys, HttpStatus.OK);
    }

    @PostMapping("/upload-single")
    public ResponseEntity<String> uploadSingleFile(@RequestParam("file") MultipartFile file) {
        String key = s3Service.uploadSingleFile(file);
        return new ResponseEntity<>(key, HttpStatus.OK);
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage(@RequestParam String key) {
        FileData fileData = s3Service.downloadFile(key);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, fileData.getContentType())
                .body(fileData.getContent());
    }

    @GetMapping("/presigned-url")
    public ResponseEntity<String> getPresignedUrl(@RequestParam String key, @RequestParam int durationInMinutes) {
        String url = s3Service.getPresignedUrl(key, durationInMinutes);
        return new ResponseEntity<>(url, HttpStatus.OK);
    }
}