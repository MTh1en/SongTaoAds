package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.background.BackgroundCreateRequest;
import com.capstone.ads.dto.background.BackgroundDTO;
import com.capstone.ads.dto.background.BackgroundUpdateRequest;
import com.capstone.ads.service.BackgroundService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "BACKGROUND")
public class BackgroundController {
    private final BackgroundService backgroundService;

    @PostMapping(
            value = "/attribute-values/{attributeValueId}/backgrounds",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Tạo background theo giá trị thuộc tính")
    public ApiResponse<BackgroundDTO> createBackground(@PathVariable String attributeValueId,
                                                       @ModelAttribute BackgroundCreateRequest request) {
        var response = backgroundService.createBackgroundByAttributeValue(attributeValueId, request);
        return ApiResponseBuilder.buildSuccessResponse("Create background successfully", response);
    }

    @PatchMapping("/backgrounds/{backgroundId}/information")
    @Operation(summary = "Cập nhật thông tin của background")
    public ApiResponse<BackgroundDTO> updateBackgroundInformation(@PathVariable String backgroundId,
                                                                  @RequestBody BackgroundUpdateRequest request) {
        var response = backgroundService.updateBackgroundInformation(backgroundId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update background information successfully", response);
    }

    @PatchMapping(value = "/backgrounds/{backgroundId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cập nhật hình ảnh của background")
    public ApiResponse<BackgroundDTO> updateBackgroundImage(@PathVariable String backgroundId,
                                                            @RequestPart MultipartFile backgroundImage) {
        var response = backgroundService.updateBackgroundImage(backgroundId, backgroundImage);
        return ApiResponseBuilder.buildSuccessResponse("Update background image successfully", response);
    }

    @GetMapping("customer-choices/{customerChoiceId}/suggestion")
    @Operation(summary = "Xem background theo lựa chọn của khách hàng")
    public ApiResponse<List<BackgroundDTO>> suggestedBackgrounds(@PathVariable String customerChoiceId) {
        var response = backgroundService.suggestedBackgrounds(customerChoiceId);
        return ApiResponseBuilder.buildSuccessResponse("Suggested backgrounds", response);
    }

    @GetMapping("/attribute-values/{attributeValueId}/backgrounds")
    @Operation(summary = "Xem background theo giá trị thuộc tính")
    public ApiPagingResponse<BackgroundDTO> findBackgroundByAttributeValue(
            @PathVariable String attributeValueId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = backgroundService.findBackgroundByAttributeValue(attributeValueId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find background by attribute value", response, page);
    }

    @GetMapping("/backgrounds")
    @Operation(summary = "Xem tất cả background")
    public ApiPagingResponse<BackgroundDTO> findBackgroundByAttributeValue(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = backgroundService.findAllBackground(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find all background", response, page);
    }

    @DeleteMapping("/backgrounds/{backgroundId}")
    @Operation(summary = "Xóa  cứng background")
    public ApiResponse<Void> deleteBackground(@PathVariable String backgroundId) {
        backgroundService.hardDeleteBackground(backgroundId);
        return ApiResponseBuilder.buildSuccessResponse("Delete background successful", null);
    }
}
