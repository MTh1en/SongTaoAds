package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.demo_design.CustomerRejectCustomDesignRequest;
import com.capstone.ads.dto.demo_design.DemoDesignCreateRequest;
import com.capstone.ads.dto.demo_design.DemoDesignDTO;
import com.capstone.ads.dto.demo_design.DesignerUpdateDescriptionCustomDesignRequest;
import com.capstone.ads.dto.file.FileDataDTO;
import com.capstone.ads.dto.file.UploadMultipleFileRequest;
import com.capstone.ads.service.DemoDesignsService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "DEMO DESIGN")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DemoDesignsController {
    DemoDesignsService demoDesignsService;

    @PostMapping(
            value = "/custom-design-requests/{customDesignRequestId}/demo-designs",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Designer gửi bản demo cho khách hàng")
    public ApiResponse<DemoDesignDTO> designerCreateCustomDesign(@PathVariable String customDesignRequestId,
                                                                 @ModelAttribute DemoDesignCreateRequest request) {
        var response = demoDesignsService.designerCreateCustomDesign(customDesignRequestId, request);
        return ApiResponseBuilder.buildSuccessResponse("Gửi demo thành công", response);
    }

    @PatchMapping("/demo-designs/{customDesignId}/approve")
    @Operation(summary = "Khách hàng chấp nhận bản demo")
    public ApiResponse<DemoDesignDTO> customerApproveCustomDesign(@PathVariable String customDesignId) {
        var response = demoDesignsService.customerApproveCustomDesign(customDesignId);
        return ApiResponseBuilder.buildSuccessResponse("Chấp nhận demo thành công", response);
    }

    @PatchMapping(value = "/demo-designs/{customDesignId}/reject", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Khách hàng từ chối bản demo")
    public ApiResponse<DemoDesignDTO> customerRejectCustomDesign(@PathVariable String customDesignId,
                                                                 @Valid @ModelAttribute CustomerRejectCustomDesignRequest request) {
        var response = demoDesignsService.customerRejectCustomDesign(customDesignId, request);
        return ApiResponseBuilder.buildSuccessResponse("Từ chối demo thành công", response);
    }

    @PatchMapping("/demo-designs/{customDesignId}/designer-description")
    @Operation(summary = "Designer cập nhật lại miêu tả cho bản thiết kế")
    public ApiResponse<DemoDesignDTO> designerUpdateDescriptionCustomDesign(@PathVariable String customDesignId,
                                                                            @RequestBody DesignerUpdateDescriptionCustomDesignRequest request) {
        var response = demoDesignsService.designerUpdateDescriptionCustomDesign(customDesignId, request);
        return ApiResponseBuilder.buildSuccessResponse("Cập nhật miêu tả thiết kế thành công", response);
    }

    @PatchMapping(value = "/demo-designs/{customDesignId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Designer cập nhật lại hình ảnh demo trước khi khách hàng approve/reject")
    public ApiResponse<DemoDesignDTO> designerUploadImage(@PathVariable String customDesignId,
                                                          @RequestPart("file") MultipartFile file) {
        var response = demoDesignsService.designerUploadImage(customDesignId, file);
        return ApiResponseBuilder.buildSuccessResponse("Cập nhật hình ảnh thành công", response);
    }

    @PostMapping(value = "/demo-designs/{customDesignId}/sub-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Design cập nhật những hình ảnh phụ cho bản demo của mình")
    public ApiResponse<List<FileDataDTO>> uploadDemoDesignSubImage(@PathVariable String customDesignId,
                                                                   @ModelAttribute UploadMultipleFileRequest request) {
        var response = demoDesignsService.uploadDemoDesignSubImages(customDesignId, request);
        return ApiResponseBuilder.buildSuccessResponse("Upload hình ảnh phụ thành công", response);
    }

    @GetMapping("/custom-design-requests/{customDesignRequestId}/demo-designs")
    @Operation(summary = "Xem lịch sử bản demo theo request")
    public ApiPagingResponse<DemoDesignDTO> findCustomDesignByCustomDesignRequest(
            @PathVariable String customDesignRequestId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = demoDesignsService.findCustomDesignByCustomDesignRequest(customDesignRequestId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem lịch sử demo theo yêu cầu thiết kế thành công", response, page);
    }

    @DeleteMapping("/demo-designs/{demoDesignId}")
    @Operation(summary = "Xóa cứng bản demo")
    public ApiResponse<Void> hardDeleteCustomDesign(@PathVariable String demoDesignId) {
        demoDesignsService.hardDeleteCustomDesign(demoDesignId);
        return ApiResponseBuilder.buildSuccessResponse("Xóa demo thành công", null);
    }
}
