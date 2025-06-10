package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.chatBot.*;
import com.capstone.ads.service.ChatBotService;
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
public class ChatBotController {
    private final ChatBotService chatBotService;

    @PostMapping("/chat")
    public ApiResponse<String> chat(@RequestBody ChatRequest request) {
        String reply = chatBotService.chat(request.getPrompt());
        return ApiResponse.<String>builder()
                .message("Chat response retrieved successfully.")
                .result(reply)
                .build();
    }

    @PostMapping(value = "/upload-file-finetune", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<FileUploadResponse> uploadFileToFinetune(
           @RequestParam  MultipartFile file) {
        FileUploadResponse response = chatBotService.uploadFileToFinetune(file);
        return ApiResponse.<FileUploadResponse>builder()
                .success(true)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .message("File uploaded successfully for fine-tuning")
                .result(response)
                .build();
    }

    @PostMapping("/finetune-model")
    public ApiResponse<FineTuningJobResponse> finetuneModel( @RequestBody FineTuningJobRequest request) {
        FineTuningJobResponse response = chatBotService.fineTuningJob(request);
        return ApiResponse.<FineTuningJobResponse>builder()
                .success(true)
                .message("Fine-tuning job created successfully")
                .result(response)
                .build();
    }

    @GetMapping("/files/{fileId}")
    public ApiResponse<FileUploadResponse> getFileById(
            @PathVariable String fileId) {
        FileUploadResponse response = chatBotService.getUploadedFileById(fileId);
        return ApiResponse.<FileUploadResponse>builder()
                .success(true)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .message("File retrieved successfully")
                .result(response)
                .build();
    }

    @GetMapping("/files")
    public ApiResponse<List<FileUploadResponse>> getFineTuningFiles() {
        List<FileUploadResponse> response = chatBotService.getAllUploadedFiles();
        return ApiResponse.<List<FileUploadResponse>>builder()
                .success(true)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .message("Fine-tuning files retrieved successfully")
                .result(response)
                .build();
    }

    @DeleteMapping("/files/{fileId}")
    public ApiResponse<FileDeletionResponse> deleteFile(
            @PathVariable String fileId) {
        FileDeletionResponse response =   chatBotService.deleteUploadedFile(fileId);
       return ApiResponse.<FileDeletionResponse>builder()
                .success(true)
                .message("File deleted successfully")
                .result(response)
                .build();
    }
    @GetMapping("/fine-tune-jobs")
    public ApiResponse<List<FineTuningJobResponse>> getFineTuningJobs() {
        List<FineTuningJobResponse> response = chatBotService.getAllFineTuneJobs();
        return ApiResponse.<List<FineTuningJobResponse>>builder()
                .success(true)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .message("Fine-tuning jobs retrieved successfully")
                .result(response)
                .build();
    }

    @PostMapping("/fine-tuning-jobs/{fineTuningJobId}/cancel")
    public ApiResponse<FineTuningJobResponse> cancelFineTuningJob(
            @PathVariable String fineTuningJobId) {
        FineTuningJobResponse response = chatBotService.cancelFineTuningJob(fineTuningJobId);
        return ApiResponse.<FineTuningJobResponse>builder()
                .success(true)
                .message("Fine-tuning job cancelled successfully")
                .result(response)
                .build();
    }

}
