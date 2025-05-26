package com.capstone.ads.repository.external;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Repository {
    List<String> uploadFiles(String bucketName, List<MultipartFile> files) throws IOException;

    String uploadSingleFile(String bucketName, MultipartFile file); // Đổi return type thành String để trả về key

    void downloadFile(String bucketName, String key, String destinationPath);

    String generatePresignedUrl(String bucketName, String key, int durationInMinutes);
}
