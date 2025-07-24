package com.capstone.ads.repository.external;

import com.capstone.ads.dto.file.FileInformation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Repository {
    String uploadSingleFile(String bucketName, MultipartFile file, String key);

    List<String> uploadMultipleFilesWithTransferManager(String bucketName, List<MultipartFile> files, List<String> keys);

    FileInformation downloadFile(String bucketName, String key);

    String generatePresignedUrl(String bucketName, String key, int durationInMinutes);

    void deleteFile(String bucketName, String key);

    List<String> deleteMultipleFiles(String bucketName, List<String> keys);

    byte[] downloadImageAsBytes(String bucketName, String key);
}
