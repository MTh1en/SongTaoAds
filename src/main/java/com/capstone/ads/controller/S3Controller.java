package com.capstone.ads.controller;

import com.capstone.ads.dto.file.FileInformation;
import com.capstone.ads.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "AWS S3")
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping(value = "/upload-single", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload 1 file lên AWS S3")
    public ResponseEntity<String> uploadSingleFile(@RequestPart String keyName,
                                                   @RequestPart("file") MultipartFile file) {
        String key = s3Service.uploadSingleFile(keyName, file);
        return new ResponseEntity<>(key, HttpStatus.OK);
    }

    @GetMapping("/image")
    @Operation(summary = "Lấy hình ảnh từ Key trên S3")
    public ResponseEntity<byte[]> getImage(@RequestParam String key) {
        FileInformation fileInformation = s3Service.downloadFile(key);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, fileInformation.getContentType())
                .body(fileInformation.getContent());
    }

    @GetMapping("/presigned-url")
    @Operation(summary = "Lấy presigned url hình ảnh trên S3")
    public ResponseEntity<String> getPresignedUrl(@RequestParam String key, @RequestParam int durationInMinutes) {
        String url = s3Service.getPresignedUrl(key, durationInMinutes);
        return new ResponseEntity<>(url, HttpStatus.OK);
    }

    @DeleteMapping("/image")
    @Operation(summary = "Xóa hình ảnh trên S3")
    public ResponseEntity<Void> deleteImage(@RequestParam String key) {
        s3Service.deleteFile(key);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/multi-image")
    @Operation(summary = "Xóa nhiều hình ảnh trên S3")
    public ResponseEntity<List<String>> deleteMultiImage(@RequestParam List<String> keys) {
        var response = s3Service.deleteMultipleFiles(keys);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}