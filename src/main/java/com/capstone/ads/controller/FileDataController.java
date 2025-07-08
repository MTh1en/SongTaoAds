package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.file.FileDataDTO;
import com.capstone.ads.dto.file.IconCreateRequest;
import com.capstone.ads.dto.file.IconUpdateInfoRequest;
import com.capstone.ads.service.FileDataService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "FILE DATA")
public class FileDataController {
    private final FileDataService fileDataService;

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

    @DeleteMapping("/icons/{iconId}")
    @Operation(summary = "Xóa cứng icon")
    public ApiResponse<Void> deleteIconSystem(@PathVariable String iconId) {
        fileDataService.deleteIconSystem(iconId);
        return ApiResponseBuilder.buildSuccessResponse("Delete icon successfully", null);
    }
}
