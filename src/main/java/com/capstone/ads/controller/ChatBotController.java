package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.chatBot.*;
import com.capstone.ads.service.ChatBotService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/chat-bot")
@RequiredArgsConstructor
@Tag(name = "CHAT BOT")
public class ChatBotController {
    private final ChatBotService chatBotService;

    @PostMapping("/chat")
    public ApiResponse<String> chat(@RequestBody ChatRequest request) {
        String reply = chatBotService.chat(request);
        return ApiResponseBuilder.buildSuccessResponse("Chat response retrieved successfully.", reply);
    }

    @PostMapping("/translate-to-txt2img-prompt")
    public ApiResponse<String> translate(@RequestBody ChatRequest request) {
        String reply = chatBotService.translateToTextToImagePrompt(request.getPrompt());
        return ApiResponseBuilder.buildSuccessResponse("Translate retrieved successfully.", reply);
    }

    @PostMapping(value = "/upload-file-finetune", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<FileUploadResponse> uploadFileToFinetune(
            @RequestParam MultipartFile file) {
        FileUploadResponse response = chatBotService.uploadFileToFinetune(file);
        return ApiResponseBuilder.buildSuccessResponse("File uploaded successfully for fine-tuning", response);
    }

    @PostMapping("/finetune-model")
    public ApiResponse<FineTuningJobResponse> finetuneModel(@RequestBody FineTuningJobRequest request) {
        FineTuningJobResponse response = chatBotService.fineTuningJob(request);
        return ApiResponseBuilder.buildSuccessResponse("Fine-tuning job created successfully", response);
    }

    @GetMapping("/files/{fileId}")
    public ApiResponse<FileUploadResponse> getFileById(
            @PathVariable String fileId) {
        FileUploadResponse response = chatBotService.getUploadedFileById(fileId);
        return ApiResponseBuilder.buildSuccessResponse("File retrieved successfully", response);
    }

    @GetMapping("/files")
    public ApiResponse<List<FileUploadResponse>> getFineTuningFiles() {
        List<FileUploadResponse> response = chatBotService.getAllUploadedFiles();
        return ApiResponseBuilder.buildSuccessResponse("Fine-tuning files retrieved successfully", response);
    }

    @DeleteMapping("/files/{fileId}")
    public ApiResponse<FileDeletionResponse> deleteFile(
            @PathVariable String fileId) {
        FileDeletionResponse response = chatBotService.deleteUploadedFile(fileId);
        return ApiResponseBuilder.buildSuccessResponse("File deleted successfully", response);
    }

    @GetMapping("/fine-tune-jobs")
    public ApiResponse<List<FineTuningJobResponse>> getFineTuningJobs() {
        List<FineTuningJobResponse> response = chatBotService.getAllFineTuneJobs();
        return ApiResponseBuilder.buildSuccessResponse("Fine tuning jobs retrieved successfully", response);
    }

    @PostMapping("/fine-tuning-jobs/{fineTuningJobId}/cancel")
    public ApiResponse<FineTuningJobResponse> cancelFineTuningJob(
            @PathVariable String fineTuningJobId) {
        FineTuningJobResponse response = chatBotService.cancelFineTuningJob(fineTuningJobId);
        return ApiResponseBuilder.buildSuccessResponse("Fine tuning job cancelled successfully", response);
    }

}
