package com.capstone.ads.service.impl;

import com.capstone.ads.repository.external.S3Repository;
import com.capstone.ads.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private static final int DEFAULT_PRESIGNED_URL_DURATION = 30;

    private final S3Repository s3Repository;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Files list cannot be null or empty");
        }

        return s3Repository.uploadFiles(bucketName, files);
    }

    @Override
    public String uploadSingleFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        return s3Repository.uploadSingleFile(bucketName, file);
    }

    @Override
    public void downloadFile(String key, String destinationPath) {
        validateKeyAndPath(key, destinationPath);
        s3Repository.downloadFile(bucketName, key, destinationPath);
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