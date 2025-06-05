package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.aidesign.AIDesignDTO;
import com.capstone.ads.service.AIDesignsService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AIDesignsController {
    private final AIDesignsService service;

    @PostMapping(value = "/customer-details/{customerDetailId}/design-templates/{designTemplateId}/ai-designs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AIDesignDTO> createAIDesign(@PathVariable String customerDetailId,
                                                   @PathVariable String designTemplateId,
                                                   @RequestPart String customerNote,
                                                   @RequestPart MultipartFile aiImage) {
        var response = service.createAIDesign(customerDetailId, designTemplateId, customerNote, aiImage);
        return ApiResponseBuilder.buildSuccessResponse("Create AIDesign successfully", response);
    }

    @GetMapping("/customer-details/{customerDetailId}/ai-designs")
    public ApiPagingResponse<AIDesignDTO> findAIDesignByCustomerDetailId(@PathVariable String customerDetailId,
                                                                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                         @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findAIDesignByCustomerDetailId(customerDetailId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find AIDesign by Customer Detail Id successfully", response, page);
    }

    @DeleteMapping("/ai-designs/{aiDesignId}")
    public ApiResponse<Void> deleteAIDesign(@PathVariable String aiDesignId) {
        service.hardDeleteAIDesign(aiDesignId);
        return ApiResponseBuilder.buildSuccessResponse("Delete AIDesign successful", null);
    }
}
