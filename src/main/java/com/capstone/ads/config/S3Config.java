package com.capstone.ads.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

@Configuration
public class S3Config {
    @Value("${aws.region}")
    private String region;

    @Bean
    public SdkAsyncHttpClient sdkAsyncHttpClient() {
        return NettyNioAsyncHttpClient.builder()
                .maxConcurrency(100) // Tăng concurrency để cải thiện tốc độ
                .maxPendingConnectionAcquires(5_000) // Tùy chỉnh nếu cần
                .build();
    }

    @Bean
    public S3AsyncClient s3AsyncClient(SdkAsyncHttpClient sdkAsyncHttpClient) {
        return S3AsyncClient.builder()
                .httpClient(sdkAsyncHttpClient)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(region))
                .multipartEnabled(true) // Bật multipart cho hiệu suất
                .build();
    }

    @Bean
    public S3TransferManager s3TransferManager(S3AsyncClient s3AsyncClient) {
        return S3TransferManager.builder()
                .s3Client(s3AsyncClient)
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(region))
                .build();
    }
}
