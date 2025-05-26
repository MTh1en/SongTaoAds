package com.capstone.ads.controller;

import com.capstone.ads.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
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

    @GetMapping("/download/{key}")
    public ResponseEntity<String> downloadFile(@PathVariable String key, @RequestParam String destinationPath) {
        s3Service.downloadFile(key, destinationPath);
        return new ResponseEntity<>("File downloaded to " + destinationPath, HttpStatus.OK);
    }

    @GetMapping("/presigned-url/{key}")
    public ResponseEntity<String> getPresignedUrl(@PathVariable String key, @RequestParam int durationInMinutes) {
        String url = s3Service.getPresignedUrl(key, durationInMinutes);
        return new ResponseEntity<>(url, HttpStatus.OK);
    }
}