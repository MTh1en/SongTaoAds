package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.file.*;
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
        return ApiResponseBuilder.buildSuccessResponse("Upload thành công", response);
    }

    @GetMapping("/file-data")
    @Operation(summary = "Xem thông tin file theo đường dẫn hình ảnh")
    public ApiResponse<FileDataDTO> findFileDataByImageUrl(@RequestParam String imageUrl) {
        var response = fileDataService.findFileDataByImageUrl(imageUrl);
        return ApiResponseBuilder.buildSuccessResponse("Xem thông tin file thành công", response);
    }

    @DeleteMapping("/file-data/{fileDataId}")
    @Operation(summary = "Xóa cứng dữ liệu file")
    public ApiResponse<Void> hardDeleteFileDatai(@PathVariable String fileDataId) {
        fileDataService.hardDeleteFileDataById(fileDataId);
        return ApiResponseBuilder.buildSuccessResponse("Xóa file thành công", null);
    }

    // ===== ICON ===== //
    @PostMapping(value = "/icons", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload icon")
    public ApiResponse<FileDataDTO> uploadIconSystem(@Valid @ModelAttribute IconCreateRequest request) {
        var response = fileDataService.uploadIconSystem(request);
        return ApiResponseBuilder.buildSuccessResponse("Upload icon thành công", response);
    }

    @PatchMapping("/icons/{iconId}/information")
    @Operation(summary = "Cập nhật thông tin của icon")
    public ApiResponse<FileDataDTO> updateIconSystemInformation(@PathVariable String iconId,
                                                                @Valid @RequestBody IconUpdateInfoRequest request) {
        var response = fileDataService.updateIconSystemInformation(iconId, request);
        return ApiResponseBuilder.buildSuccessResponse("Cập nhật thông tin icon thành công", response);
    }

    @PatchMapping(value = "/icons/{iconId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cập nhật hình ảnh icon")
    public ApiResponse<FileDataDTO> updateIconSystemImage(@PathVariable String iconId,
                                                          @RequestPart MultipartFile iconImage) {
        var response = fileDataService.updateIconSystemImage(iconId, iconImage);
        return ApiResponseBuilder.buildSuccessResponse("Cập nhật hình ảnh icon thành công", response);
    }

    @GetMapping("/icons")
    @Operation(summary = "Xem tất cả icon trong hệ thống")
    public ApiPagingResponse<FileDataDTO> findAllIconSystem(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        var response = fileDataService.findAllIconSystem(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem tất cả icon", response, page);
    }

    // ==== DEMO DESIGN ===== //
    @GetMapping("/demo-designs/{demoDesignId}/sub-images")
    @Operation(summary = "Xem những hình ảnh phụ có trong bản demo")
    public ApiResponse<List<FileDataDTO>> findFileDataByDemoDesignId(@PathVariable String demoDesignId) {
        var response = fileDataService.findFileDataByDemoDesignId(demoDesignId);
        return ApiResponseBuilder.buildSuccessResponse("Xem những hình ảnh phụ có trong bản demo thành công", response);
    }

    // ===== CUSTOM DESIGN REQUEST ===== //
    @GetMapping("/custom-design-requests/{customDesignRequestId}/sub-images")
    @Operation(summary = "Xem những hình ảnh phụ có trong bản thiết kế chính thức")
    public ApiResponse<List<FileDataDTO>> findFileDataByCustomDesignRequestId(@PathVariable String customDesignRequestId) {
        var response = fileDataService.findFileDataByCustomDesignRequestId(customDesignRequestId);
        return ApiResponseBuilder.buildSuccessResponse(
                "Xem những hình ảnh phụ có trong bản thiết kế chính thức thành công",
                response);
    }

    // ===== PROGRESS LOG ===== //
    @GetMapping("/progress-logs/{progressLogId}/images")
    @Operation(summary = "Xem những hình ảnh trong tiến trình của đơn hàng")
    public ApiResponse<List<FileDataDTO>> findFileDataByProgressLogId(@PathVariable String progressLogId) {
        var response = fileDataService.findFileDataByProgressLogId(progressLogId);
        return ApiResponseBuilder.buildSuccessResponse("Xem những hình ảnh trong tiến trình của đơn hàng thành công", response);
    }
}
