package com.capstone.ads.repository.external;

import com.capstone.ads.dto.file.FileData;

import java.util.List;

public interface S3Repository {
    List<String> uploadFiles(String bucketName, List<byte[]> fileContents, List<String> keys, List<String> contentTypes);

    String uploadSingleFile(String bucketName, byte[] fileContent, String key, String contentType);

    FileData downloadFile(String bucketName, String key);

    String generatePresignedUrl(String bucketName, String key, int durationInMinutes);
}
