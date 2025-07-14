package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.file.*;
import com.capstone.ads.model.enums.FileTypeEnum;
import com.capstone.ads.service.FileDataService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "FILE DATA")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileDataController {
    FileDataService fileDataService;

    @PostMapping(value = "file-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload 1 File và lưu xuống FileData")
    public ApiResponse<FileDataDTO> uploadFileData(@RequestPart String key, @RequestPart MultipartFile file) {
        var response = fileDataService.uploadSingleFile(key, file);
        return ApiResponseBuilder.buildSuccessResponse("Upload successful", response);
    }

    @GetMapping("/file-data")
    @Operation(summary = "Xem thông tin file theo đường dẫn hình ảnh")
    public ApiResponse<FileDataDTO> findFileDataByImageUrl(@RequestParam String imageUrl) {
        var response = fileDataService.findFileDataByImageUrl(imageUrl);
        return ApiResponseBuilder.buildSuccessResponse("Find successful", response);
    }

    @DeleteMapping("/file-data/{fileDataId}")
    @Operation(summary = "Xóa cứng dữ liệu file")
    public ApiResponse<Void> hardDeleteFileDatai(@PathVariable String fileDataId) {
        fileDataService.hardDeleteFileDataById(fileDataId);
        return ApiResponseBuilder.buildSuccessResponse("Delete file data successfully", null);
    }

    // ===== ICON ===== //
    @PostMapping(value = "/icons", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload icon")
    public ApiResponse<FileDataDTO> uploadIconSystem(@Valid @ModelAttribute IconCreateRequest request) {
        var response = fileDataService.uploadIconSystem(request);
        return ApiResponseBuilder.buildSuccessResponse("Successfully uploaded file", response);
    }

    @PatchMapping("/icons/{iconId}/information")
    @Operation(summary = "Cập nhật thông tin của icon")
    public ApiResponse<FileDataDTO> updateIconSystemInformation(@PathVariable String iconId,
                                                                @Valid @RequestBody IconUpdateInfoRequest request) {
        var response = fileDataService.updateIconSystemInformation(iconId, request);
        return ApiResponseBuilder.buildSuccessResponse("Successfully updated icon", response);
    }

    @PatchMapping(value = "/icons/{iconId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cập nhật hình ảnh icon")
    public ApiResponse<FileDataDTO> updateIconSystemImage(@PathVariable String iconId,
                                                          @RequestPart MultipartFile iconImage) {
        var response = fileDataService.updateIconSystemImage(iconId, iconImage);
        return ApiResponseBuilder.buildSuccessResponse("Successfully updated icon", response);
    }

    @GetMapping("/icons")
    @Operation(summary = "Xem tất cả icon trong hệ thống")
    public ApiPagingResponse<FileDataDTO> findAllIconSystem(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        var response = fileDataService.findAllIconSystem(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find all icon successful", response, page);
    }

    // ===== ORDER SUB IMAGE ===== /

    @GetMapping("/orders/{orderId}/sub-images")
    @Operation(summary = "Xem tất cả hình ảnh phụ trong Order theo từng trạng thái")
    public ApiResponse<List<FileDataDTO>> findOrderSubImagesByOrderIdAndFileType(
            @PathVariable String orderId,
            @RequestParam FileTypeEnum fileType) {
        var response = fileDataService.findFileDataByOrderIdAndFileType(orderId, fileType);
        return ApiResponseBuilder.buildSuccessResponse("Find order sub images successful", response);
    }

    // ==== DEMO DESIGN ===== //
    @GetMapping("/demo-designs/{demoDesignId}/sub-images")
    @Operation(summary = "Xem những hình ảnh phụ có trong bản demo")
    public ApiResponse<List<FileDataDTO>> findFileDataByDemoDesignId(@PathVariable String demoDesignId) {
        var response = fileDataService.findFileDataByDemoDesignId(demoDesignId);
        return ApiResponseBuilder.buildSuccessResponse("Find demo design sub images successful", response);
    }

    // ===== CUSTOM DESIGN REQUEST ===== //
    @GetMapping("/custom-design-requests/{customDesignRequestId}/sub-images")
    @Operation(summary = "Xem những hình ảnh phụ có trong bản thiết kế chính thức")
    public ApiResponse<List<FileDataDTO>> findFileDataByCustomDesignRequestId(@PathVariable String customDesignRequestId) {
        var response = fileDataService.findFileDataByCustomDesignRequestId(customDesignRequestId);
        return ApiResponseBuilder.buildSuccessResponse("Find custom design request sub images successful", response);
    }
}
