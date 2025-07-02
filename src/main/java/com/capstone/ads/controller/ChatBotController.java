package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
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

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/chat-bot")
@RequiredArgsConstructor
@Tag(name = "CHAT BOT")
public class ChatBotController {
    private final ChatBotService chatBotService;

    @PostMapping("/chat")
    @Operation(summary = "Chat với chatbot")
    public ApiResponse<String> chat(@RequestBody ChatRequest request) {
        String reply = chatBotService.chat(request);
        return ApiResponseBuilder.buildSuccessResponse("Chat response retrieved successfully.", reply);
    }

    @PostMapping("/test-chat")
    @Operation(summary = "Staff Test Model Chat")
    public ApiResponse<String> chat(@RequestBody TestChatRequest request) {
        String reply = chatBotService.TestChat(request);
        return ApiResponseBuilder.buildSuccessResponse("Chat response retrieved successfully.", reply);
    }


    @PostMapping("/translate-to-txt2img-prompt")
    public ApiResponse<String> translate(@RequestBody ChatRequest request) {
        String reply = chatBotService.translateToTextToImagePrompt(request.getPrompt());
        return ApiResponseBuilder.buildSuccessResponse("Translate retrieved successfully.", reply);
    }

    @PostMapping(value = "/upload-file-finetune", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload file để fine-tune")
    public ApiResponse<FileUploadResponse> uploadFileToFinetune(
            @RequestParam MultipartFile file) {
        FileUploadResponse response = chatBotService.uploadFileToFineTune(file);
        return ApiResponseBuilder.buildSuccessResponse("File uploaded successfully for fine-tuning", response);
    }

    @PostMapping("/finetune-model")
    @Operation(summary = "File-tune model hiện tại")
    public ApiResponse<FineTuningJobResponse> finetuneModel(@RequestBody FineTuningJobRequest request) {
        FineTuningJobResponse response = chatBotService.fineTuningJob(request);
        return ApiResponseBuilder.buildSuccessResponse("Fine-tuning job created successfully", response);
    }

    @GetMapping("/files/{fileId}")
    @Operation(summary = "Xem chi tiết file")
    public ApiResponse<FileUploadResponse> getFileById(
            @PathVariable String fileId) {
        FileUploadResponse response = chatBotService.getUploadedFileById(fileId);
        return ApiResponseBuilder.buildSuccessResponse("File retrieved successfully", response);
    }

    @GetMapping("/files")
    @Operation(summary = "Xem tất cả file uploaded để fine-tune")
    public ApiResponse<List<FileUploadResponse>> getFineTuningFiles() {
        List<FileUploadResponse> response = chatBotService.getAllUploadedFiles();
        return ApiResponseBuilder.buildSuccessResponse("Fine-tuning files retrieved successfully", response);
    }

    @DeleteMapping("/files/{fileId}")
    @Operation(summary = "Xóa file đã upload")
    public ApiResponse<FileDeletionResponse> deleteFile(
            @PathVariable String fileId) {
        FileDeletionResponse response = chatBotService.deleteUploadedFile(fileId);
        return ApiResponseBuilder.buildSuccessResponse("File deleted successfully", response);
    }

    @GetMapping("/fine-tune-jobs")
    @Operation(summary = "Xem tất cả các job đã fine-tune")
    public ApiResponse<List<FineTuningJobResponse>> getFineTuningJobs() {
        List<FineTuningJobResponse> response = chatBotService.getAllFineTuneJobs();
        return ApiResponseBuilder.buildSuccessResponse("Fine tuning jobs retrieved successfully", response);
    }

    @GetMapping("/{fineTuneJobId}/fine-tune-jobs")
    @Operation(summary = "Xem chi tiết job đã fine-tune")
    public ApiResponse<FineTuningJobResponse> getFineTuningJobById(@PathVariable String fineTuneJobId) {
        FineTuningJobResponse response = chatBotService.getFineTuningJob(fineTuneJobId);
        return ApiResponseBuilder.buildSuccessResponse("Fine tuning job retrieved successfully", response);
    }

    @PostMapping("/{fineTuningJobId}/fine-tuning-jobs/cancel")
    @Operation(summary = "Hủy 1 job đang fine-tune")
    public ApiResponse<FineTuningJobResponse> cancelFineTuningJob(
            @PathVariable String fineTuningJobId) {
        FineTuningJobResponse response = chatBotService.cancelFineTuningJob(fineTuningJobId);
        return ApiResponseBuilder.buildSuccessResponse("Fine tuning job cancelled successfully", response);
    }

    @PostMapping("/{fineTuningJobId}/fine-tuning-jobs/select-model")
    @Operation(summary = "Chọn model để chat từ list job")
    public ApiResponse<ModelChatBotDTO> selectModelChat(
            @PathVariable String fineTuningJobId) {
        ModelChatBotDTO response = chatBotService.setModeChatBot(fineTuningJobId);
        return ApiResponseBuilder.buildSuccessResponse("Fine tuning job cancelled successfully", response);
    }

    @PostMapping(value = "/upload-file-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Convert từ file excel thành file jsonl")
    public ApiResponse<String> uploadFileExcel(@RequestParam("file") MultipartFile file,
                                             @RequestParam("fileName") String fileName) {
        String resposne = chatBotService.uploadFileExcel(file, fileName);
        return ApiResponseBuilder.buildSuccessResponse(("Uploaded file successfully"), resposne);
    }

    @GetMapping("/models")
    @Operation(summary = "Xem tất cả các model open-ai")
    public ApiResponse<ListModelsResponse> getModels() {
        ListModelsResponse response = chatBotService.getModels();
        return ApiResponseBuilder.buildSuccessResponse("Models retrieved successfully", response);
    }

    @GetMapping("/models-fine-tune")
    @Operation(summary = "Xem tất cả các model đã fine-tune")
    public ApiPagingResponse<ModelChatBotDTO> viewAllModelFineTune(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var models = chatBotService.getModelChatBots(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Tickets retrieved successfully", models, page);
    }
}
