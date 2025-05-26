package com.capstone.ads.service.impl;

import com.capstone.ads.dto.file.FileData;
import com.capstone.ads.repository.external.S3Repository;
import com.capstone.ads.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private static final int DEFAULT_PRESIGNED_URL_DURATION = 30;

    private final S3Repository s3Repository;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Files list cannot be null or empty");
        }

        // Chuyển MultipartFile thành byte[] và tạo keys
        List<byte[]> fileContents = files.stream()
                .map(file -> {
                    try {
                        return file.getBytes();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to read file: " + file.getOriginalFilename(), e);
                    }
                })
                .toList();

        List<String> keys = files.stream()
                .map(file -> generateUniqueKey(file.getOriginalFilename()))
                .toList();

        List<String> contentTypes = files.stream()
                .map(MultipartFile::getContentType)
                .toList();
        return s3Repository.uploadFiles(bucketName, fileContents, keys, contentTypes);
    }

    @Override
    public String uploadSingleFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        try {
            byte[] fileContent = file.getBytes();
            String key = generateUniqueKey(file.getOriginalFilename());
            String contentType = file.getContentType();
            return s3Repository.uploadSingleFile(bucketName, fileContent, key, contentType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public FileData downloadFile(String key) {
        validateKey(key);
        return s3Repository.downloadFile(bucketName, key);
    }

    @Override
    public String getPresignedUrl(String key) {
        return getPresignedUrl(key, DEFAULT_PRESIGNED_URL_DURATION);
    }

    @Override
    public String getPresignedUrl(String key, int durationInMinutes) {
        validateKey(key);
        if (durationInMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }

        return s3Repository.generatePresignedUrl(bucketName, key, durationInMinutes);
    }

    private String generateUniqueKey(String fileName) {
        return UUID.randomUUID() + "-" + fileName;
    }

    private void validateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
    }

    private void validateKeyAndPath(String key, String path) {
        validateKey(key);
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination path cannot be null or empty");
        }
    }
}