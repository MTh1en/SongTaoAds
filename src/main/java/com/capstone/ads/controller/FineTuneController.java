package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.chatBot.FileDeletionResponse;
import com.capstone.ads.dto.chatBot.FileUploadResponse;
import com.capstone.ads.dto.chatBot.FineTuningJobRequest;
import com.capstone.ads.dto.chatBot.FineTuningJobResponse;
import com.capstone.ads.service.FineTuneService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/fine-tune")
@RequiredArgsConstructor
@Tag(name = "FINE TUNE")
public class FineTuneController {
    private final FineTuneService fineTuneService;

    @PostMapping(value = "/upload-file-finetune", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload file để fine-tune")
    public ApiResponse<FileUploadResponse> uploadFileToFinetune(
            @RequestParam MultipartFile file) {
        FileUploadResponse response = fineTuneService.uploadFileToFineTune(file);
        return ApiResponseBuilder.buildSuccessResponse("File uploaded successfully for fine-tuning", response);
    }

    @PostMapping("/finetune-model")
    @Operation(summary = "File-tune model hiện tại")
    public ApiResponse<FineTuningJobResponse> finetuneModel(@RequestBody FineTuningJobRequest request) {
        FineTuningJobResponse response = fineTuneService.fineTuningJob(request);
        return ApiResponseBuilder.buildSuccessResponse("Fine-tuning job created successfully", response);
    }

    @GetMapping("/files/{fileId}")
    @Operation(summary = "Xem thông tin file")
    public ApiResponse<FileUploadResponse> getFileById(
            @PathVariable String fileId) {
        FileUploadResponse response = fineTuneService.getUploadedFileById(fileId);
        return ApiResponseBuilder.buildSuccessResponse("File retrieved successfully", response);
    }

    @GetMapping(value = "/files/{fileId}/content", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Xem nội dung file")
    public ResponseEntity<byte[]> getFileContentById(@PathVariable String fileId) {
        var response = fineTuneService.getContentFileById(fileId);
        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/files")
    @Operation(summary = "Xem tất cả file uploaded để fine-tune")
    public ApiResponse<List<FileUploadResponse>> getFineTuningFiles() {
        List<FileUploadResponse> response = fineTuneService.getAllUploadedFiles();
        return ApiResponseBuilder.buildSuccessResponse("Fine-tuning files retrieved successfully", response);
    }

    @DeleteMapping("/files/{fileId}")
    @Operation(summary = "Xóa file đã upload")
    public ApiResponse<FileDeletionResponse> deleteFile(
            @PathVariable String fileId) {
        FileDeletionResponse response = fineTuneService.deleteUploadedFile(fileId);
        return ApiResponseBuilder.buildSuccessResponse("File deleted successfully", response);
    }

    @GetMapping("/fine-tune-jobs")
    @Operation(summary = "Xem tất cả các job đã fine-tune")
    public ApiResponse<List<FineTuningJobResponse>> getFineTuningJobs() {
        List<FineTuningJobResponse> response = fineTuneService.getAllFineTuneJobs();
        return ApiResponseBuilder.buildSuccessResponse("Fine tuning jobs retrieved successfully", response);
    }

    @GetMapping("/{fineTuneJobId}/fine-tune-jobs")
    @Operation(summary = "Xem chi tiết job đã fine-tune")
    public ApiResponse<FineTuningJobResponse> getFineTuningJobById(@PathVariable String fineTuneJobId) {
        FineTuningJobResponse response = fineTuneService.getFineTuningJob(fineTuneJobId);
        return ApiResponseBuilder.buildSuccessResponse("Fine tuning job retrieved successfully", response);
    }

    @PostMapping("/{fineTuningJobId}/fine-tuning-jobs/cancel")
    @Operation(summary = "Hủy 1 job đang fine-tune")
    public ApiResponse<FineTuningJobResponse> cancelFineTuningJob(
            @PathVariable String fineTuningJobId) {
        FineTuningJobResponse response = fineTuneService.cancelFineTuningJob(fineTuningJobId);
        return ApiResponseBuilder.buildSuccessResponse("Fine tuning job cancelled successfully", response);
    }
}
