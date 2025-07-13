package com.capstone.ads.repository.external.impl;

import com.capstone.ads.dto.file.FileInformation;
import com.capstone.ads.repository.external.S3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.Upload;
import software.amazon.awssdk.transfer.s3.model.UploadRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Repository
@RequiredArgsConstructor
@Slf4j
public class S3RepositoryImpl implements S3Repository {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3TransferManager s3TransferManager;

    @Override
    public String uploadSingleFile(String bucketName, MultipartFile file, String key) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return key;
        } catch (IOException e) {
            log.error("IO Exception while uploading file with key {}", key, e);
            throw new RuntimeException("Upload failed", e);
        }
    }

    @Override
    public List<String> uploadMultipleFilesWithTransferManager(String bucketName, List<MultipartFile> files, List<String> keys) {
        List<CompletableFuture<String>> futures = new ArrayList<>();

        IntStream.range(0, files.size()).forEach(i -> {
            MultipartFile file = files.get(i);
            String key = keys.get(i);
            try {
                UploadRequest uploadRequest = UploadRequest.builder()
                        .putObjectRequest(req -> req
                                .bucket(bucketName)
                                .key(key)
                                .contentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream"))
                        .requestBody(AsyncRequestBody.fromBytes(file.getBytes()))
                        .build();

                Upload upload = s3TransferManager.upload(uploadRequest);
                CompletableFuture<String> future = upload.completionFuture().thenApply(result -> {
                    log.info("Successfully uploaded file with key: {}", key);
                    return key;
                }).exceptionally(throwable -> {
                    log.error("Failed to upload file with key: {}", key, throwable);
                    return null; // Return null for failed uploads
                });
                futures.add(future);
            } catch (IOException e) {
                log.error("Error preparing upload for file with key: {}", key, e);
                futures.add(CompletableFuture.completedFuture(null));
            }
        });

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    @Override
    public FileInformation downloadFile(String bucketName, String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try (ResponseInputStream<GetObjectResponse> res = s3Client.getObject(getObjectRequest)) {
            // Lấy contentType từ response, không phải request
            String contentType = res.response().contentType() != null ? res.response().contentType() : "application/octet-stream";
            byte[] content = res.readAllBytes(); // Nạp vào RAM, phù hợp với file nhỏ
            return FileInformation.builder()
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
    public void deleteFile(String bucketName, String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            log.error("Failed to delete file with key: {}", key, e);
            throw new RuntimeException("Delete failed for file with key: " + key, e);
        }
    }

    @Override
    public List<String> deleteMultipleFiles(String bucketName, List<String> keys) {
        try {
            List<ObjectIdentifier> objectsToDelete = keys.stream()
                    .map(key -> ObjectIdentifier.builder().key(key).build())
                    .toList();

            DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                    .bucket(bucketName)
                    .delete(Delete.builder().objects(objectsToDelete).build())
                    .build();

            DeleteObjectsResponse response = s3Client.deleteObjects(deleteObjectsRequest);

            List<String> deletedKeys = response.deleted().stream()
                    .map(DeletedObject::key)
                    .toList();

            if (response.hasErrors()) {
                response.errors().forEach(error ->
                        log.error("Failed to delete file with key: {}. Error: {}", error.key(), error.message()));
            }

            return deletedKeys;
        } catch (Exception e) {
            log.error("Failed to delete multiple files", e);
            throw new RuntimeException("Delete multiple files failed", e);
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
