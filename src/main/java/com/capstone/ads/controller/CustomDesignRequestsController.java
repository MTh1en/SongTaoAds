package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestDTO;
import com.capstone.ads.dto.file.FileDataDTO;
import com.capstone.ads.dto.file.UploadMultipleFileRequest;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import com.capstone.ads.service.CustomDesignRequestService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "CUSTOM DESIGN REQUEST")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomDesignRequestsController {
    CustomDesignRequestService service;

    @PostMapping("/customer-details/{customerDetailId}/customer-choices/{customerChoiceId}")
    @Operation(summary = "Tạo thông yêu cầu thiết kế tùy chỉnh")
    public ApiResponse<CustomDesignRequestDTO> createCustomDesignRequest(
            @PathVariable String customerDetailId,
            @PathVariable String customerChoiceId,
            @Valid @RequestBody CustomDesignRequestCreateRequest request) {
        var response = service.createCustomDesignRequest(customerDetailId, customerChoiceId, request);
        return ApiResponseBuilder.buildSuccessResponse("Create custom design request successful", response);
    }

    @PatchMapping("/custom-design-requests/{customDesignRequestId}/users/{designerId}")
    @Operation(summary = "Chia Task cho Designer")
    public ApiResponse<CustomDesignRequestDTO> assignDesignerToCustomerRequest(@PathVariable String customDesignRequestId,
                                                                               @PathVariable String designerId) {
        var response = service.assignDesignerToCustomerRequest(customDesignRequestId, designerId);
        return ApiResponseBuilder.buildSuccessResponse("Assign custom design request successful", response);
    }

    @PatchMapping("/custom-design-requests/{customDesignRequestId}/approve")
    @Operation(summary = "Designer chấp nhận yêu cầu")
    public ApiResponse<CustomDesignRequestDTO> approveCustomDesignRequest(@PathVariable String customDesignRequestId) {
        var response = service.designerApproveCustomDesignRequest(customDesignRequestId);
        return ApiResponseBuilder.buildSuccessResponse("Designer approved custom design request assigned", response);
    }

    @PatchMapping("/custom-design-requests/{customDesignRequestId}/reject")
    @Operation(summary = "Designer từ chối yêu cầu")
    public ApiResponse<CustomDesignRequestDTO> rejectCustomDesignRequest(@PathVariable String customDesignRequestId) {
        var response = service.designerRejectCustomDesignRequest(customDesignRequestId);
        return ApiResponseBuilder.buildSuccessResponse("Designer rejected custom design request assigned", response);
    }

    @PatchMapping(
            value = "/custom-design-requests/{customDesignRequestId}/final-design-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Designer gửi bản thiết kế chính thức")
    public ApiResponse<CustomDesignRequestDTO> designerUploadFinalDesignImage(@PathVariable String customDesignRequestId,
                                                                              @RequestPart MultipartFile finalDesignImage) {
        var response = service.designerUploadFinalDesignImage(customDesignRequestId, finalDesignImage);
        return ApiResponseBuilder.buildSuccessResponse("Designer rejected custom design request assigned", response);
    }

    @PostMapping(
            value = "/custom-design-requests/{customDesignRequestId}/final-design-sub-images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Design gửi hình ảnh phụ của bản thiết kế chính thức")
    public ApiResponse<List<FileDataDTO>> uploadCustomDesignRequestSubImages(@PathVariable String customDesignRequestId,
                                                                             @ModelAttribute UploadMultipleFileRequest request) {
        var response = service.uploadCustomDesignRequestSubImages(customDesignRequestId, request);
        return ApiResponseBuilder.buildSuccessResponse("Upload sub images successfully", response);
    }

    @GetMapping("/customer-details/{customerDetailId}/custom-design-requests")
    @Operation(summary = "Xem yêu cầu thiết kế theo thông tin doanh nghiệp")
    public ApiPagingResponse<CustomDesignRequestDTO> findCustomerDesignRequestByCustomerDetailId(
            @PathVariable String customerDetailId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findCustomerDesignRequestByCustomerDetailId(customerDetailId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse(
                "Find custom design request by custom detail request successful",
                response, page
        );
    }

    @GetMapping("/users/{designerId}/custom-design-requests")
    @Operation(summary = "Designer xem những yêu cầu thiết kế được phân công cho mình")
    public ApiPagingResponse<CustomDesignRequestDTO> findCustomerDetailRequestByAssignDesignerId(
            @PathVariable String designerId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findCustomerDetailRequestByAssignDesignerId(designerId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse(
                "Find custom design request by designer request successful",
                response, page
        );
    }

    @GetMapping("/custom-design-requests")
    @Operation(summary = "Xem yêu cầu thiết kế theo trạng thái")
    public ApiPagingResponse<CustomDesignRequestDTO> findCustomerDetailRequestByStatus(
            @RequestParam("status") CustomDesignRequestStatus status,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findCustomerDetailRequestByStatus(status, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find custom design request by status successful",
                response, page
        );
    }
}
