package com.capstone.ads.service.impl;

import com.capstone.ads.dto.chatBot.*;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ChatBotLogMapper;
import com.capstone.ads.model.ChatBotLog;
import com.capstone.ads.repository.external.ChatBotRepository;
import com.capstone.ads.repository.internal.ChatBotLogRepository;
import com.capstone.ads.service.ChatBotService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;
    @Value("${spring.ai.openai.chat.options.model}")
    private String modelName;

    private final ChatBotLogRepository chatBotLogRepository;
    private final SecurityContextUtils securityContextUtils;
    private final ChatBotRepository chatBotRepository;
    private final ChatBotLogMapper chatBotLogMapper;

    @Override
    public String chat(ChatRequest request) {
        ChatCompletionRequest completionRequest = new ChatCompletionRequest();
        completionRequest.setModel(modelName);
        completionRequest.setMessages(List.of(
                new ChatCompletionRequest.Message("system", "Bạn là trợ lý AI tư vấn về thiết kế và in ấn biển quảng cáo."),
                new ChatCompletionRequest.Message("user", request.getPrompt())
        ));
        ChatCompletionResponse response = chatBotRepository.getChatCompletions(
                "Bearer " + openaiApiKey,
                completionRequest);
        saveResponse(response, request.getPrompt());
        return response.getChoices().getFirst().getMessage().getContent();
    }

    @Override
    public String translateToTextToImagePrompt(String prompt) {
        ChatCompletionRequest completionRequest = new ChatCompletionRequest();
        completionRequest.setModel(modelName);
        completionRequest.setMessages(List.of(
                new ChatCompletionRequest.Message("system", "Based on the customer's request, write a prompt in English to create an image of the billboard. Do not create an image, only answer the prompt in English."),
                new ChatCompletionRequest.Message("user", prompt)
        ));
        ChatCompletionResponse response = chatBotRepository.getChatCompletions(
                "Bearer " + openaiApiKey,
                completionRequest);
        return response.getChoices().getFirst().getMessage().getContent();
    }

    @Override
    public FileUploadResponse uploadFileToFinetune(MultipartFile file) {
        // Validate inputs
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        // Validate file type
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".jsonl")) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        // Call OpenAI file upload API
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
    public FineTuningJobResponse cancelFineTuningJob(String fineTuningJobId) {

        if (fineTuningJobId == null || fineTuningJobId.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        return chatBotRepository.cancelFineTuningJob(
                "Bearer " + openaiApiKey,
                fineTuningJobId);
    }

    private void saveResponse(ChatCompletionResponse response, String question) {
        var userId = securityContextUtils.getCurrentUserId();
        // Extract answer from response
        String answer = (response.getChoices() != null && !response.getChoices().isEmpty())
                ? response.getChoices().getFirst().getMessage().getContent()
                : null;
        if (answer == null || answer.trim().isEmpty()) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
        ChatBotLog log = chatBotLogMapper.toEntity(question, answer, userId);
        chatBotLogRepository.save(log);
    }
}