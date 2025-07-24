package com.capstone.ads.service;

import com.capstone.ads.dto.file.FileInformation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {
    String uploadSingleFile(String skeyName, MultipartFile file);

    List<String> uploadMultipleFiles(List<MultipartFile> files, List<String> keyNames);

    FileInformation downloadFile(String key);

    String getPresignedUrl(String key, int durationInMinutes);

    void deleteFile(String key);

    List<String> deleteMultipleFiles(List<String> keys);
}
