package com.capstone.ads.repository.external.impl;

import com.capstone.ads.dto.file.FileData;
import com.capstone.ads.repository.external.S3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;

@Repository
@RequiredArgsConstructor
@Slf4j
public class S3RepositoryImpl implements S3Repository {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Override
    public String uploadSingleFile(String bucketName, MultipartFile file, String key) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                    .build();
            log.info("content type: {}", file.getContentType());
            // Stream file trực tiếp, không nạp vào RAM
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return key;
        } catch (IOException e) {
            log.error("IO Exception while uploading file with key {}", key, e);
            throw new RuntimeException("Upload failed", e);
        }
    }

    @Override
    public FileData downloadFile(String bucketName, String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try (ResponseInputStream<GetObjectResponse> res = s3Client.getObject(getObjectRequest)) {
            // Lấy contentType từ response, không phải request
            String contentType = res.response().contentType() != null ? res.response().contentType() : "application/octet-stream";
            byte[] content = res.readAllBytes(); // Nạp vào RAM, phù hợp với file nhỏ
            return FileData.builder()
                    .content(content)
                    .contentType(contentType)
                    .build();
        } catch (IOException e) {
            log.error("IO Exception while downloading file with key {}", key, e);
            throw new RuntimeException("Download failed", e);
        }
    }

    @Override
    public String generatePresignedUrl(String bucketName, String key, int durationInMinutes) {
        try {
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(durationInMinutes))
                    .getObjectRequest(req -> req.bucket(bucketName).key(key))
                    .build();

            return s3Presigner.presignGetObject(presignRequest).url().toString();
        } catch (Exception e) {
            log.error("Failed to generate presigned URL for key: {}", key, e);
            throw new RuntimeException("Failed to generate presigned URL", e);
        }
    }

    @Override
    public byte[] downloadImageAsBytes(String bucketName, String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        try (ResponseInputStream<GetObjectResponse> res = s3Client.getObject(getObjectRequest)) {
            return res.readAllBytes();
        } catch (IOException e) {
            log.error("Failed to download image from S3. Bucket: {}, Key: {}", bucketName, key, e);
            throw new RuntimeException("Image download failed", e);
        }
    }
}
