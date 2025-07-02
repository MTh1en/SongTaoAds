package com.capstone.ads.service;

import com.capstone.ads.dto.chatBot.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatBotService {
    String chat(ChatRequest request);

    String TestChat(TestChatRequest request);

    String translateToTextToImagePrompt(String prompt);

    FileUploadResponse uploadFileToFineTune(MultipartFile file);

    FineTuningJobResponse fineTuningJob(FineTuningJobRequest request);

    FileUploadResponse getUploadedFileById(String fileId); // New method

    List<FileUploadResponse> getAllUploadedFiles();

    FileDeletionResponse deleteUploadedFile(String fileId);

    List<FineTuningJobResponse> getAllFineTuneJobs();

    FineTuningJobResponse getFineTuningJob(String jobId);

    FineTuningJobResponse cancelFineTuningJob(String fineTuningJobId);

    FileUploadResponse uploadFileExcel(MultipartFile file, String fileName);

    ListModelsResponse getModels();

    Page<ModelChatBotDTO> getModelChatBots(int page, int size);

    ModelChatBotDTO setModeChatBot(String jobId);
}
