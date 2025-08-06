package com.capstone.ads.service.impl;

import com.capstone.ads.dto.chatBot.*;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ModelChatBotMapper;
import com.capstone.ads.model.ModelChatBot;
import com.capstone.ads.repository.external.ChatBotRepository;
import com.capstone.ads.repository.internal.ModelChatBotRepository;
import com.capstone.ads.service.FineTuneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FineTuneServiceImpl implements FineTuneService {
    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;

    private final ChatBotRepository chatBotRepository;
    private final ModelChatBotRepository modelChatBotRepository;
    private final ModelChatBotMapper modelChatBotMapper;

    @Override
    public FileUploadResponse uploadFileToFineTune(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".jsonl")) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        return chatBotRepository.uploadFile(
                "Bearer " + openaiApiKey,
                "fine-tune",
                file);
    }

    @Override
    public FineTuningJobResponse fineTuningJob(FineTuningJobRequest request) {
        // Call OpenAI fine-tuning API
        FineTuningJobResponse response;
        response = chatBotRepository.createFineTuningJob(
                "Bearer " + openaiApiKey,
                request);
        return response;
    }

    @Override
    public FileUploadResponse getUploadedFileById(String fileId) {
        if (fileId == null || fileId.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        return chatBotRepository.getFileById(
                "Bearer " + openaiApiKey,
                fileId);
    }

    @Override
    public byte[] getContentFileById(String fileId) {
        return chatBotRepository.retrieveFileBytes(
                "Bearer " + openaiApiKey,
                fileId);
    }

    @Override
    public List<FileUploadResponse> getAllUploadedFiles() {
        FileUploadedListResponse response = chatBotRepository.getFiles(
                "Bearer " + openaiApiKey);
        return response.getData();
    }

    @Override
    public FileDeletionResponse deleteUploadedFile(String fileId) {
        if (fileId == null || fileId.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        return chatBotRepository.deleteFileById(
                "Bearer " + openaiApiKey,
                fileId);
    }

    @Override
    public List<FineTuningJobResponse> getAllFineTuneJobs() {
        FineTuningJobListResponse response = chatBotRepository.getFineTuningJobs(
                "Bearer " + openaiApiKey);
        return response.getData();
    }

    @Override
    public FineTuningJobResponse getFineTuningJob(String jobId) {
        FineTuningJobResponse response = chatBotRepository.getFineTuningJobById(
                "Bearer " + openaiApiKey
                , jobId);
        if ("succeeded".equalsIgnoreCase(response.getStatus())) {
            String newModelName = response.getFineTunedModel();
            ModelChatBotDTO modelChatBotDTO = new ModelChatBotDTO();
            modelChatBotDTO.setModelName(newModelName);
            modelChatBotDTO.setPreviousModelName(response.getModel());
            modelChatBotDTO.setActive(false);

            ModelChatBot modelChatBot = modelChatBotMapper.toEntity(modelChatBotDTO);
            modelChatBotRepository.save(modelChatBot);
        }
        return response;
    }

    @Override
    public FineTuningJobResponse cancelFineTuningJob(String fineTuningJobId) {

        if (fineTuningJobId == null || fineTuningJobId.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        return chatBotRepository.cancelFineTuningJob(
                "Bearer " + openaiApiKey,
                fineTuningJobId);
    }
}
