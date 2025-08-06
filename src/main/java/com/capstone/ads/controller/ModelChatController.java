package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.chatBot.FileUploadResponse;
import com.capstone.ads.dto.chatBot.ModelChatBotDTO;
import com.capstone.ads.service.ModelChatService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/model-chat")
@RequiredArgsConstructor
@Tag(name = "MODEL CHAT")
public class ModelChatController {
    private final ModelChatService modelChatService;

    @PostMapping(value = "/upload-file-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Convert từ file excel thành file jsonl")
    public ApiResponse<FileUploadResponse> uploadFileExcel(@RequestParam("file") MultipartFile file) {
        FileUploadResponse response = modelChatService.uploadFileExcel(file);
        return ApiResponseBuilder.buildSuccessResponse(("Uploaded file successfully"), response);
    }

    @GetMapping("/models-fine-tune")
    @Operation(summary = "Xem tất cả các model đã fine-tune")
    public ApiPagingResponse<ModelChatBotDTO> viewAllModelFineTune(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var models = modelChatService.getModelChatBots(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Models retrieved successfully", models, page);
    }

    @PostMapping("/{modelChatId}/fine-tuning-jobs/select-model")
    @Operation(summary = "Chọn model để chat từ list model")
    public ApiResponse<ModelChatBotDTO> selectModelChat(
            @PathVariable String modelChatId) {
        ModelChatBotDTO response = modelChatService.setModeChatBot(modelChatId);
        return ApiResponseBuilder.buildSuccessResponse("Model chat selected successfully", response);
    }
}
