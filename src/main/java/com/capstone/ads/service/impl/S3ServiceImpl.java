package com.capstone.ads.service.impl;

import com.capstone.ads.dto.file.FileData;
import com.capstone.ads.repository.external.S3Repository;
import com.capstone.ads.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    @Value("${aws.bucket.name}")
    private String bucketName;

    private final S3Repository s3Repository;

    @Override
    public String uploadSingleFile(String keyName, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }
        return s3Repository.uploadSingleFile(bucketName, file, keyName);
    }

    @Override
    public FileData downloadFile(String key) {
        validateKey(key);
        return s3Repository.downloadFile(bucketName, key);
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
}