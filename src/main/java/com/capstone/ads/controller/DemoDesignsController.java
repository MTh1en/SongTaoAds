package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.demo_design.CustomerRejectCustomDesignRequest;
import com.capstone.ads.dto.demo_design.DemoDesignCreateRequest;
import com.capstone.ads.dto.demo_design.DemoDesignDTO;
import com.capstone.ads.dto.demo_design.DesignerUpdateDescriptionCustomDesignRequest;
import com.capstone.ads.service.DemoDesignsService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "DEMO DESIGN")
public class DemoDesignsController {
    private final DemoDesignsService demoDesignsService;

    @PostMapping(
            value = "/custom-design-requests/{customDesignRequestId}/demo-designs",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Designer gửi bản demo cho khách hàng")
    public ApiResponse<DemoDesignDTO> designerCreateCustomDesign(@PathVariable String customDesignRequestId,
                                                                 @ModelAttribute DemoDesignCreateRequest request) {
        var response = demoDesignsService.designerCreateCustomDesign(customDesignRequestId, request);
        return ApiResponseBuilder.buildSuccessResponse("Create custom design successful", response);
    }

    @PatchMapping("/demo-designs/{customDesignId}/approve")
    @Operation(summary = "Khách hàng chấp nhận bản demo")
    public ApiResponse<DemoDesignDTO> customerApproveCustomDesign(@PathVariable String customDesignId) {
        var response = demoDesignsService.customerApproveCustomDesign(customDesignId);
        return ApiResponseBuilder.buildSuccessResponse("Custom design decision successful", response);
    }

    @PatchMapping("/demo-designs/{customDesignId}/reject")
    @Operation(summary = "Khách hàng từ chối bản demo")
    public ApiResponse<DemoDesignDTO> customerRejectCustomDesign(@PathVariable String customDesignId,
                                                                 @RequestBody CustomerRejectCustomDesignRequest request) {
        var response = demoDesignsService.customerRejectCustomDesign(customDesignId, request);
        return ApiResponseBuilder.buildSuccessResponse("Custom design decision successful", response);
    }

    @PatchMapping(
            value = "/demo-designs/{customDesignId}/feedback-images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Khách hàng gửi hình ảnh feedback (Optional nếu khách hàng có ảnh feedback, ko thì chỉ reject thôi)")
    public ApiResponse<DemoDesignDTO> customerUploadFeedbackImage(@PathVariable String customDesignId,
                                                                  @RequestPart MultipartFile customDesignImage) {
        var response = demoDesignsService.customerUploadFeedbackImage(customDesignId, customDesignImage);
        return ApiResponseBuilder.buildSuccessResponse("Custom design decision successful", response);
    }

    @PatchMapping("/demo-designs/{customDesignId}/designer-description")
    @Operation(summary = "Designer cập nhật lại miêu tả cho bản thiết kế")
    public ApiResponse<DemoDesignDTO> designerUpdateDescriptionCustomDesign(@PathVariable String customDesignId,
                                                                            @RequestBody DesignerUpdateDescriptionCustomDesignRequest request) {
        var response = demoDesignsService.designerUpdateDescriptionCustomDesign(customDesignId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update design description successful", response);
    }

    @PatchMapping(value = "/demo-designs/{customDesignId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Designer cập nhật lại hình ảnh demo trước khi khách hàng approve/reject")
    public ApiResponse<DemoDesignDTO> designerUploadImage(@PathVariable String customDesignId,
                                                          @RequestPart("file") MultipartFile file) {
        var response = demoDesignsService.designerUploadImage(customDesignId, file);
        return ApiResponseBuilder.buildSuccessResponse("Upload image successful", response);
    }

    @GetMapping("/custom-design-requests/{customDesignRequestId}/demo-designs")
    @Operation(summary = "Xem lịch sử bạn demo theo request")
    public ApiPagingResponse<DemoDesignDTO> findCustomDesignByCustomDesignRequest(@PathVariable String customDesignRequestId,
                                                                                  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = demoDesignsService.findCustomDesignByCustomDesignRequest(customDesignRequestId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find Custom Design by Custom Design Request successful", response, page);
    }

    @DeleteMapping("/demo-designs/{demoDesignId}")
    @Operation(summary = "Xóa cứng bản demo")
    public ApiResponse<Void> hardDeleteCustomDesign(@PathVariable String demoDesignId) {
        demoDesignsService.hardDeleteCustomDesign(demoDesignId);
        return ApiResponseBuilder.buildSuccessResponse("Delete Custom Design successful", null);
    }
}
