package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.designtemplate.DesignTemplateCreateRequest;
import com.capstone.ads.dto.designtemplate.DesignTemplateDTO;
import com.capstone.ads.dto.designtemplate.DesignTemplateUpdateRequest;
import com.capstone.ads.service.DesignTemplatesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DesignTemplatesController {
    private final DesignTemplatesService designTemplatesService;

    @PostMapping("/product-types/{productTypeId}/design-templates")
    public ApiResponse<DesignTemplateDTO> createDesignTemplate(@PathVariable("productTypeId") String productTypeId,
                                                               @RequestBody DesignTemplateCreateRequest request) {
        var response = designTemplatesService.createDesignTemplate(productTypeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Create Design Template successful!", response);
    }

    @PutMapping("/design-templates/{designTemplateId}/information")
    public ApiResponse<DesignTemplateDTO> updateDesignTemplateInformation(@PathVariable String designTemplateId,
                                                                          @RequestBody DesignTemplateUpdateRequest request) {
        var response = designTemplatesService.updateDesignTemplateInformation(designTemplateId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update Design Template information successful!", response);
    }

    @PutMapping(value = "/design-templates/{designTemplateId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<DesignTemplateDTO> updateDesignTemplateImage(@PathVariable String designTemplateId,
                                                                    @RequestPart("file") MultipartFile designTemplateImage) {
        var response = designTemplatesService.uploadDesignTemplateImage(designTemplateId, designTemplateImage);
        return ApiResponseBuilder.buildSuccessResponse("Update Design Template image successful!", response);
    }

    @GetMapping("/design-templates/{designTemplateId}")
    public ApiResponse<DesignTemplateDTO> findDesignTemplateById(@PathVariable String designTemplateId) {
        var response = designTemplatesService.findDesignTemplateById(designTemplateId);
        return ApiResponseBuilder.buildSuccessResponse("Find Design Template successful!", response);
    }

    @GetMapping("product-types/{productTypeId}/design-templates")
    public ApiResponse<List<DesignTemplateDTO>> findDesignTemplateByProductTypeId(@PathVariable String productTypeId) {
        var response = designTemplatesService.findDesignTemplateByProductTypeId(productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Find Design Template by ProductTypeId successful!", response);
    }

    @GetMapping("/design-templates")
    public ApiResponse<List<DesignTemplateDTO>> findAllDesignTemplates() {
        var response = designTemplatesService.findAllDesignTemplates();
        return ApiResponseBuilder.buildSuccessResponse("Find All Design Template successful!", response);
    }

    @DeleteMapping("/design-templates/{designTemplateId}")
    public ApiResponse<Void> hardDeleteDesignTemplate(@PathVariable String designTemplateId) {
        designTemplatesService.hardDeleteDesignTemplate(designTemplateId);
        return ApiResponseBuilder.buildSuccessResponse("Hard delete Design Template successful!", null);
    }

    @PatchMapping("/design-templates/{designTemplateId}/{isAvailable}")
    public ApiResponse<Void> softDeleteDesignTemplate(@PathVariable String designTemplateId,
                                                      @PathVariable Boolean isAvailable) {
        designTemplatesService.softDeleteDesignTemplate(designTemplateId, isAvailable);
        return ApiResponseBuilder.buildSuccessResponse("Soft delete Design Template successful!", null);
    }
}
