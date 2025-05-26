package com.capstone.ads.repository.external;

import java.util.List;

public interface S3Repository {
    List<String> uploadFiles(String bucketName, List<byte[]> fileContents, List<String> keys, List<String> contentTypes);

    String uploadSingleFile(String bucketName, byte[] fileContent, String key, String contentType);

    void downloadFile(String bucketName, String key, String destinationPath);

    String generatePresignedUrl(String bucketName, String key, int durationInMinutes);
}
