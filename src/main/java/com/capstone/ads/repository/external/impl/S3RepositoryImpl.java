package com.capstone.ads.repository.external.impl;

import com.capstone.ads.repository.external.S3Repository;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class S3RepositoryImpl implements S3Repository {
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final Duration RETRY_DELAY = Duration.ofMillis(500);

    private final S3AsyncClient s3AsyncClient;
    private final S3TransferManager transferManager;
    private final S3Presigner s3Presigner;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public List<String> uploadFiles(String bucketName, List<byte[]> fileContents, List<String> keys, List<String> contentTypes) {
        if (fileContents.size() != keys.size() || fileContents.size() != contentTypes.size()) {
            throw new IllegalArgumentException("Number of file contents, keys, and content types must match");
        }

        List<CompletableFuture<String>> futures = new ArrayList<>();
        for (int i = 0; i < fileContents.size(); i++) {
            futures.add(uploadFileWithRetry(bucketName, fileContents.get(i), keys.get(i), contentTypes.get(i)));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    private CompletableFuture<String> uploadFileWithRetry(String bucketName, byte[] fileContent, String key, String contentType) {
        return CompletableFuture.supplyAsync(() -> {
            int attempt = 0;
            while (attempt < MAX_RETRY_ATTEMPTS) {
                try {
                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(contentType != null ? contentType : "application/octet-stream") // Sử dụng contentType, fallback nếu null
                            .build();

                    s3AsyncClient.putObject(
                            putObjectRequest,
                            AsyncRequestBody.fromBytes(fileContent)
                    ).join();

                    return key;
                } catch (Exception e) {
                    attempt++;
                    if (attempt == MAX_RETRY_ATTEMPTS) {
                        log.error("Failed to upload file with key {} after {} attempts", key, attempt, e);
                        return null;
                    }
                    try {
                        Thread.sleep(RETRY_DELAY.toMillis());
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        log.error("Thread interrupted during retry delay", ie);
                        return null;
                    }
                }
            }
            return null;
        }, executorService);
    }

    @Override
    public String uploadSingleFile(String bucketName, byte[] fileContent, String key, String contentType) {
        return uploadFileWithRetry(bucketName, fileContent, key, contentType).join();
    }


    @Override
    public void downloadFile(String bucketName, String key, String destinationPath) {
        try {
            transferManager.downloadFile(d -> d
                            .getObjectRequest(req -> req.bucket(bucketName).key(key))
                            .destination(Paths.get(destinationPath))
                            .addTransferListener(LoggingTransferListener.create()))
                    .completionFuture()
                    .join();
        } catch (Exception e) {
            log.error("Failed to download file with key: {} to path: {}", key, destinationPath, e);
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

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
