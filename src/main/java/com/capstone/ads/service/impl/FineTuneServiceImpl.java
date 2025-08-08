package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageKeyFormat;
import com.capstone.ads.dto.fine_tune.FileDeletionResponse;
import com.capstone.ads.dto.fine_tune.FileUploadResponse;
import com.capstone.ads.dto.fine_tune.FineTuningJobRequest;
import com.capstone.ads.dto.fine_tune.FineTuningJobResponse;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.repository.external.ChatBotClient;
import com.capstone.ads.service.FineTuneService;
import com.capstone.ads.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FineTuneServiceImpl implements FineTuneService {
    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;

    private final ChatBotClient chatBotClient;
    private final S3Service s3Service;

    @Override
    public FileUploadResponse uploadFileToFineTune(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.toLowerCase().endsWith(".jsonl")) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }

            String newFileName = String.format(S3ImageKeyFormat.FINE_TUNE_FILE, UUID.randomUUID());

            MultipartFile newFile = new MockMultipartFile("file", newFileName,
                    "application/octet-stream", file.getBytes());
            s3Service.uploadSingleFile(newFileName, newFile);
            return chatBotClient.uploadFile(
                    "Bearer " + openaiApiKey,
                    "fine-tune",
                    newFile);
        } catch (IOException e) {
            log.info("Error while converting jsonl file", e);
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
    }

    @Override
    public FineTuningJobResponse fineTuningJob(FineTuningJobRequest request) {
        return chatBotClient.createFineTuningJob(
                "Bearer " + openaiApiKey,
                request);
    }

    @Override
    public FileUploadResponse getUploadedFileById(String fileId) {
        return chatBotClient.getFileById(
                "Bearer " + openaiApiKey,
                fileId);
    }

    @Override
    public byte[] getContentFileById(String fileId) {
        return chatBotClient.retrieveFileBytes(
                "Bearer " + openaiApiKey,
                fileId);
    }

    @Override
    public List<FileUploadResponse> getAllUploadedFiles() {
        return chatBotClient.getFiles(
                "Bearer " + openaiApiKey).getData();
    }

    @Override
    public FileDeletionResponse deleteUploadedFile(String fileId) {
        return chatBotClient.deleteFileById(
                "Bearer " + openaiApiKey,
                fileId);
    }

    @Override
    public List<FineTuningJobResponse> getAllFineTuneJobs() {
        return chatBotClient.getFineTuningJobs(
                "Bearer " + openaiApiKey).getData();
    }

    @Override
    public FineTuningJobResponse getFineTuningJob(String jobId) {
        return chatBotClient.getFineTuningJobById(
                "Bearer " + openaiApiKey, jobId);
    }

    @Override
    public FineTuningJobResponse cancelFineTuningJob(String fineTuningJobId) {
        if (fineTuningJobId == null || fineTuningJobId.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        return chatBotClient.cancelFineTuningJob(
                "Bearer " + openaiApiKey,
                fineTuningJobId);
    }
}
