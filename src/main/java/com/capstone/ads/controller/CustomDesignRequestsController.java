package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestDTO;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestFinalDesignRequest;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "CUSTOM DESIGN REQUEST")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomDesignRequestsController {
    CustomDesignRequestService service;

    @PostMapping("/customer-details/{customerDetailId}/custom-design-requests")
    @Operation(summary = "Tạo thông yêu cầu thiết kế tùy chỉnh")
    public ApiResponse<CustomDesignRequestDTO> createCustomDesignRequest(
            @PathVariable String customerDetailId,
            @Valid @RequestBody CustomDesignRequestCreateRequest request) {
        var response = service.createCustomDesignRequest(customerDetailId, request);
        return ApiResponseBuilder.buildSuccessResponse("Tạo yêu cầu thiết kế tùy chỉnh thành công", response);
    }

    @PatchMapping("/custom-design-requests/{customDesignRequestId}/users/{designerId}")
    @Operation(summary = "Chia Task cho Designer")
    public ApiResponse<CustomDesignRequestDTO> assignDesignerToCustomerRequest(@PathVariable String customDesignRequestId,
                                                                               @PathVariable String designerId) {
        var response = service.assignDesignerToCustomerRequest(customDesignRequestId, designerId);
        return ApiResponseBuilder.buildSuccessResponse("Chia task cho Designer thành công", response);
    }

    @PatchMapping("/custom-design-requests/{customDesignRequestId}/approve")
    @Operation(summary = "Designer chấp nhận yêu cầu")
    public ApiResponse<CustomDesignRequestDTO> approveCustomDesignRequest(@PathVariable String customDesignRequestId) {
        var response = service.designerApproveCustomDesignRequest(customDesignRequestId);
        return ApiResponseBuilder.buildSuccessResponse("Designer chấp nhận task được chia thành công", response);
    }

    @PatchMapping("/custom-design-requests/{customDesignRequestId}/reject")
    @Operation(summary = "Designer từ chối yêu cầu")
    public ApiResponse<CustomDesignRequestDTO> rejectCustomDesignRequest(@PathVariable String customDesignRequestId) {
        var response = service.designerRejectCustomDesignRequest(customDesignRequestId);
        return ApiResponseBuilder.buildSuccessResponse("Designer từ chối task được chia thành công", response);
    }

    @PatchMapping(
            value = "/custom-design-requests/{customDesignRequestId}/final-design-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Designer gửi bản thiết kế chính thức")
    public ApiResponse<CustomDesignRequestDTO> designerUploadFinalDesignImage(@PathVariable String customDesignRequestId,
                                                                              @ModelAttribute CustomDesignRequestFinalDesignRequest request) {
        var response = service.designerUploadFinalDesignImage(customDesignRequestId, request);
        return ApiResponseBuilder.buildSuccessResponse("Designer gửi bản thiết kế chính thức thành công", response);
    }

    @GetMapping("/customer-details/{customerDetailId}/custom-design-requests")
    @Operation(summary = "Xem yêu cầu thiết kế theo thông tin doanh nghiệp")
    public ApiPagingResponse<CustomDesignRequestDTO> findCustomerDesignRequestByCustomerDetailId(
            @PathVariable String customerDetailId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findCustomDesignRequestByCustomerDetailId(customerDetailId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse(
                "Xem yêu cầu thiết kế theo thông tin doanh nghiệp",
                response, page
        );
    }

    @GetMapping("/users/{designerId}/custom-design-requests")
    @Operation(summary = "Designer xem những yêu cầu thiết kế được phân công cho mình")
    public ApiPagingResponse<CustomDesignRequestDTO> findCustomerDesignRequestByAssignDesignerId(
            @PathVariable String designerId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findCustomDesignRequestByAssignDesignerId(designerId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse(
                "Xem những yêu cầu thiết kế được chia cho designer thành công",
                response, page
        );
    }

    @GetMapping(value = "/custom-design-requests", params = "status")
    @Operation(summary = "Xem yêu cầu thiết kế theo trạng thái")
    public ApiPagingResponse<CustomDesignRequestDTO> findCustomerDesignRequestByStatus(
            @RequestParam(value = "status", required = false) CustomDesignRequestStatus status,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findCustomDesignRequestByStatus(status, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem yêu cầu thiết kế theo trạng thái thành công",
                response, page
        );
    }

    @GetMapping(value = "/custom-design-requests/need-support")
    @Operation(summary = "Xem yêu cầu thiết kế theo trạng thái")
    public ApiPagingResponse<CustomDesignRequestDTO> findAllCustomDesignRequestNeedSupport(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findAllCustomDesignRequestNeedSupport(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem yêu cầu thiết kế cần hỗ trợ thành công",
                response, page
        );
    }

    @GetMapping(value = "/custom-design-requests", params = "!status")
    @Operation(summary = "Xem yêu cầu thiết kế theo trạng thái")
    public ApiPagingResponse<CustomDesignRequestDTO> findAllCustomDesignRequest(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findAllCustomerDesignRequest(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem tất cả yêu cầu thiết kế thành công",
                response, page
        );
    }
}
