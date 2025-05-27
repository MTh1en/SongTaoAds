package com.capstone.ads.service;

import com.capstone.ads.dto.file.FileData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {
    List<String> uploadFiles(List<MultipartFile> files) throws IOException;

    String uploadSingleFile(MultipartFile file);

    FileData downloadFile(String key);

    String getPresignedUrl(String key);

    String getPresignedUrl(String key, int durationInMinutes);
}
