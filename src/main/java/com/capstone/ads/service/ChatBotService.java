package com.capstone.ads.service;

import com.capstone.ads.dto.chatBot.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatBotService {
    String chat(ChatRequest request);

    String translateToTextToImagePrompt(String prompt);

    FileUploadResponse uploadFileToFinetune(MultipartFile file);

    FineTuningJobResponse fineTuningJob(FineTuningJobRequest request);

    FileUploadResponse getUploadedFileById(String fileId); // New method

    List<FileUploadResponse> getAllUploadedFiles();

    FileDeletionResponse deleteUploadedFile(String fileId);

    List<FineTuningJobResponse> getAllFineTuneJobs();

    FineTuningJobResponse cancelFineTuningJob(String fineTuningJobId);

}
