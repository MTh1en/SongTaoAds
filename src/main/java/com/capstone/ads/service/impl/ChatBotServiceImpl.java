package com.capstone.ads.service.impl;

import com.capstone.ads.dto.chatBot.*;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.model.ChatBotLog;
import com.capstone.ads.repository.external.ChatBotRepository;
import com.capstone.ads.repository.internal.ChatBotLogRepository;
import com.capstone.ads.repository.internal.UsersRepository;
import com.capstone.ads.service.ChatBotService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    private final ChatBotRepository openAiClient;
    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;
    private final UsersRepository userRepository;
    @Value("${spring.ai.openai.chat.options.model}")
    private String modelName;
    private final ChatBotLogRepository chatBotRepository;
    private final UsersRepository usersRepository;
    private final SecurityContextUtils securityContextUtils;
    @Override
    public String chat(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(modelName);
        request.setMessages(List.of(
                new ChatCompletionRequest.Message("system", "Bạn là trợ lý AI tư vấn về thiết kế và in ấn biển quảng cáo."),
                new ChatCompletionRequest.Message("user", prompt)
        ));
        ChatCompletionResponse response = openAiClient.getChatCompletions(
                "Bearer " + openaiApiKey,
                request
        );
        // Validate response
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }

        // Extract content
        String content = response.getChoices().get(0).getMessage().getContent();
        if (content == null || content.trim().isEmpty()) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }

        // Save response to database
        saveResponse(response, prompt);
        return response.getChoices().get(0).getMessage().getContent();
    }

    public void saveResponse(ChatCompletionResponse response, String question) {
        // Validate inputs
        var user = securityContextUtils.getCurrentUser();
        if (response == null) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        if (user == null) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        if (question == null || question.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        // Extract answer from response
        String answer = (response.getChoices() != null && !response.getChoices().isEmpty())
                ? response.getChoices().get(0).getMessage().getContent()
                : null;
        if (answer == null || answer.trim().isEmpty()) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }

        // Create and populate ChatBotLog entity
        ChatBotLog log = new ChatBotLog();
        log.setUsers(user);
        log.setQuestion(question);
        log.setAnswer(answer);
        log.setCreatedAt(LocalDateTime.now()); // Will be overridden by @CreationTimestamp

        // Save to database
        chatBotRepository.save(log);
    }
    @Override
    public FileUploadResponse uploadFileToFinetune(MultipartFile file){
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
            FileUploadResponse response = openAiClient.uploadFile(
                    "Bearer " + openaiApiKey,
                    "fine-tune",
                    file
            );

        return response;
        }
       @Override
       public FineTuningJobResponse fineTuningJob(FineTuningJobRequest request){
           // Call OpenAI fine-tuning API
           FineTuningJobResponse response;
               response = openAiClient.createFineTuningJob(
                       "Bearer " + openaiApiKey,
                       request
               );
           return response;

        }
    @Override
    public FileUploadResponse getUploadedFileById(String fileId) {
        if (fileId == null || fileId.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        FileUploadResponse response;
        try {
            response = openAiClient.getFileById(
                    "Bearer " + openaiApiKey,
                    fileId
            );
        } catch (Exception e) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
        if (response == null || response.getId() == null) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
        return response;
    }

    public List<FileUploadResponse> getAllUploadedFiles() {
        FileUploadedListResponse response;
        try {
            response = openAiClient.getFiles(
                    "Bearer " + openaiApiKey
            );
        }catch (Exception e) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
        return response.getData();
    }
    @Override
    public FileDeletionResponse deleteUploadedFile(String fileId) {
        if (fileId == null || fileId.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        FileDeletionResponse response;
        try {
            response = openAiClient.deleteFileById(
                    "Bearer " + openaiApiKey,
                    fileId
            );
        }catch (Exception e) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
        if (response == null || response.getId() == null) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
        return response;
    }


    @Override
    public List<FineTuningJobResponse> getAllFineTuneJobs() {
        FineTuningJobListResponse response;
        try {
            response = openAiClient.getFineTuningJobs(
                    "Bearer " + openaiApiKey
            );
        }catch (Exception e) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
        return response.getData();
    }
    public FineTuningJobResponse cancelFineTuningJob(String fineTuningJobId) {

        if (fineTuningJobId == null || fineTuningJobId.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        FineTuningJobResponse response;
        try {
            response = openAiClient.cancelFineTuningJob(
                    "Bearer " + openaiApiKey,
                    fineTuningJobId
            );
        } catch (Exception e) {

            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
        if (response == null || response.getId() == null) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
        return response;
    }

}