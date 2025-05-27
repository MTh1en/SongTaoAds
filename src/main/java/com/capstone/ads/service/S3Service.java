package com.capstone.ads.service;

import com.capstone.ads.dto.file.FileData;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String uploadSingleFile(MultipartFile file);

    FileData downloadFile(String key);

    String getPresignedUrl(String key, int durationInMinutes);
}
