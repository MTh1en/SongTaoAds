package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.demo_design.DemoDesignDTO;
import com.capstone.ads.dto.demo_design.CustomerDecisionCustomDesignRequest;
import com.capstone.ads.dto.demo_design.DesignerUpdateDescriptionCustomDesignRequest;
import com.capstone.ads.service.DemoDesignsService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DemoDesignsController {
    private final DemoDesignsService demoDesignsService;

    @PostMapping(value = "/custom-design-requests/{customDesignRequestId}/custom-designs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<DemoDesignDTO> designerCreateCustomDesign(@PathVariable String customDesignRequestId,
                                                                 @RequestPart String designerDescription,
                                                                 @RequestPart MultipartFile customDesignImage) {
        var response = demoDesignsService.designerCreateCustomDesign(customDesignRequestId, designerDescription, customDesignImage);
        return ApiResponseBuilder.buildSuccessResponse("Create custom design successful", response);
    }

    @PatchMapping("/custom-designs/{customDesignId}/customer-decision")
    public ApiResponse<DemoDesignDTO> customerDecisionCustomDesign(@PathVariable String customDesignId,
                                                                   @RequestBody CustomerDecisionCustomDesignRequest request) {
        var response = demoDesignsService.customerDecisionCustomDesign(customDesignId, request);
        return ApiResponseBuilder.buildSuccessResponse("Custom design decision successful", response);
    }

    @PatchMapping("/custom-designs/{customDesignId}/designer-description")
    public ApiResponse<DemoDesignDTO> designerUpdateDescriptionCustomDesign(@PathVariable String customDesignId,
                                                                            @RequestBody DesignerUpdateDescriptionCustomDesignRequest request) {
        var response = demoDesignsService.designerUpdateDescriptionCustomDesign(customDesignId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update design description successful", response);
    }

    @PatchMapping(value = "/custom-designs/{customDesignId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<DemoDesignDTO> designerUploadImage(@PathVariable String customDesignId,
                                                          @RequestPart("file") MultipartFile file) {
        var response = demoDesignsService.designerUploadImage(customDesignId, file);
        return ApiResponseBuilder.buildSuccessResponse("Upload image successful", response);
    }

    @GetMapping("/custom-design-requests/{customDesignRequestId}/custom-designs")
    public ApiPagingResponse<DemoDesignDTO> findCustomDesignByCustomDesignRequest(@PathVariable String customDesignRequestId,
                                                                                  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = demoDesignsService.findCustomDesignByCustomDesignRequest(customDesignRequestId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find Custom Design by Custom Design Request successful", response, page);
    }

    @DeleteMapping("/custom-designs/{customDesignId}")
    public ApiResponse<Void> hardDeleteCustomDesign(@PathVariable String customDesignId) {
        demoDesignsService.hardDeleteCustomDesign(customDesignId);
        return ApiResponseBuilder.buildSuccessResponse("Delete Custom Design successful", null);
    }
}
