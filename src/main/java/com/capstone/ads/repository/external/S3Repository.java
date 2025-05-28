package com.capstone.ads.repository.external;

import com.capstone.ads.dto.file.FileData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Repository {
    String uploadSingleFile(String bucketName, MultipartFile file, String key);

    FileData downloadFile(String bucketName, String key);

    String generatePresignedUrl(String bucketName, String key, int durationInMinutes);
}
