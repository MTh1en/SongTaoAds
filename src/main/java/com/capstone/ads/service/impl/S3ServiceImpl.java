package com.capstone.ads.service.impl;

import com.capstone.ads.dto.file.FileInformation;
import com.capstone.ads.repository.external.S3Repository;
import com.capstone.ads.service.S3Service;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class S3ServiceImpl implements S3Service {
    @NonFinal
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
    public List<String> uploadMultipleFiles(List<MultipartFile> files, List<String> keyNames) {
        return s3Repository.uploadMultipleFilesWithTransferManager(bucketName, files, keyNames);
    }

    @Override
    public FileInformation downloadFile(String key) {
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

    @Override
    public void deleteFile(String key) {
        s3Repository.deleteFile(bucketName, key);
    }

    @Override
    public List<String> deleteMultipleFiles(List<String> keys) {
        return s3Repository.deleteMultipleFiles(bucketName, keys);
    }

    private void validateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
    }
}