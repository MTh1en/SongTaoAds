package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customdesign.CustomDesignDTO;
import com.capstone.ads.dto.customdesign.CustomerDecisionCustomDesignRequest;
import com.capstone.ads.dto.customdesign.DesignerUpdateDescriptionCustomDesignRequest;
import com.capstone.ads.service.CustomDesignsService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomDesignsController {
    private final CustomDesignsService customDesignsService;

    @PostMapping(value = "/custom-design-requests/{customDesignRequestId}/custom-designs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CustomDesignDTO> designerCreateCustomDesign(@PathVariable String customDesignRequestId,
                                                                   @RequestPart String designerDescription,
                                                                   @RequestPart MultipartFile customDesignImage) {
        var response = customDesignsService.designerCreateCustomDesign(customDesignRequestId, designerDescription, customDesignImage);
        return ApiResponseBuilder.buildSuccessResponse("Create custom design successful", response);
    }

    @PatchMapping("/custom-designs/{customDesignId}/customer-decision")
    public ApiResponse<CustomDesignDTO> customerDecisionCustomDesign(@PathVariable String customDesignId,
                                                                     @RequestBody CustomerDecisionCustomDesignRequest request) {
        var response = customDesignsService.customerDecisionCustomDesign(customDesignId, request);
        return ApiResponseBuilder.buildSuccessResponse("Custom design decision successful", response);
    }

    @PatchMapping("/custom-designs/{customDesignId}/designer-description")
    public ApiResponse<CustomDesignDTO> designDTOApiResponse(@PathVariable String customDesignId,
                                                             @RequestBody DesignerUpdateDescriptionCustomDesignRequest request) {
        var response = customDesignsService.designerUpdateDescriptionCustomDesign(customDesignId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update design description successful", response);
    }

    @PatchMapping(value = "/custom-designs/{customDesignId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CustomDesignDTO> designerUploadImage(@PathVariable String customDesignId,
                                                            @RequestPart("file") MultipartFile file) {
        var response = customDesignsService.designerUploadImage(customDesignId, file);
        return ApiResponseBuilder.buildSuccessResponse("Upload image successful", response);
    }

    @GetMapping("/custom-design-requests/{customDesignRequestId}/custom-designs")
    public ApiResponse<List<CustomDesignDTO>> findCustomDesignByCustomDesignRequest(@PathVariable String customDesignRequestId) {
        var response = customDesignsService.findCustomDesignByCustomDesignRequest(customDesignRequestId);
        return ApiResponseBuilder.buildSuccessResponse("Find Custom Design by Custom Design Request successful", response);
    }

    @DeleteMapping("/custom-designs/{customDesignId}")
    public ApiResponse<Void> deleteCustomDesign(@PathVariable String customDesignId) {
        customDesignsService.hardDeleteCustomDesign(customDesignId);
        return ApiResponseBuilder.buildSuccessResponse("Delete Custom Design successful", null);
    }
}
