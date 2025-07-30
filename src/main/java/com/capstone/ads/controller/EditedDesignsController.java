package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.edited_design.EditedDesignCreateRequest;
import com.capstone.ads.dto.edited_design.EditedDesignDTO;
import com.capstone.ads.service.EditedDesignService;
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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "EDITED DESIGN")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EditedDesignsController {
    EditedDesignService service;

    @PostMapping(
            value = "/customer-details/{customerDetailId}/design-templates/{designTemplateId}/edited-designs",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Lưu ảnh sau khi chỉnh sửa")
    public ApiResponse<EditedDesignDTO> createEditedDesignFromDesignTemplate(
            @PathVariable String customerDetailId,
            @PathVariable String designTemplateId,
            @Valid @ModelAttribute EditedDesignCreateRequest request) {
        var response = service.createEditedDesignFromDesignTemplate(customerDetailId, designTemplateId, request);
        return ApiResponseBuilder.buildSuccessResponse("Create Edited Design successfully", response);
    }

    @PostMapping(
            value = "/customer-details/{customerDetailId}/backgrounds/{backgroundId}/edited-designs",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Lưu ảnh sau khi chỉnh sửa")
    public ApiResponse<EditedDesignDTO> createEditedDesignFromBackground(
            @PathVariable String customerDetailId,
            @PathVariable String backgroundId,
            @Valid @ModelAttribute EditedDesignCreateRequest request) {
        var response = service.createEditedDesignFromBackground(customerDetailId, backgroundId, request);
        return ApiResponseBuilder.buildSuccessResponse("Create Edited Design successfully", response);
    }

    @GetMapping("/customer-details/{customerDetailId}/edited-designs")
    @Operation(summary = "Xem tất cả ảnh thiết kế theo thông tin doanh nghiệp")
    public ApiPagingResponse<EditedDesignDTO> findEditedDesignByCustomerDetailId(
            @PathVariable String customerDetailId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findEditedDesignByCustomerDetailId(customerDetailId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find Edited Design by Customer Detail Id successfully", response, page);
    }

    @GetMapping("/edited-designs/{editedDesignId}")
    @Operation(summary = "Xem ảnh thiết kế theo Id")
    public ApiResponse<EditedDesignDTO> findEditedDesignByCustomerDetailId(@PathVariable String editedDesignId) {
        var response = service.findEditedDesignById(editedDesignId);
        return ApiResponseBuilder.buildSuccessResponse("Find Edited Design by Id successfully", response);
    }

    @GetMapping("/edited-designs")
    @Operation(summary = "Xem tất cả ảnh tự thiết kế")
    public ApiPagingResponse<EditedDesignDTO> findEditedDesignByCustomerDetailIds(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findAllEditedDesign(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find All Edited Designs successfully", response, page);
    }

    @DeleteMapping("/edited-designs/{editedDesignId}")
    @Operation(summary = "Xóa cứng hình ảnh thiết kế")
    public ApiResponse<Void> hardDeleteEditedDesign(@PathVariable String editedDesignId) {
        service.hardDeleteEditedDesign(editedDesignId);
        return ApiResponseBuilder.buildSuccessResponse("Delete Edited Design successful", null);
    }
}
