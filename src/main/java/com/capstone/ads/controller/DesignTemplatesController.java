package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.design_template.DesignTemplateCreateRequest;
import com.capstone.ads.dto.design_template.DesignTemplateDTO;
import com.capstone.ads.dto.design_template.DesignTemplateUpdateRequest;
import com.capstone.ads.service.DesignTemplatesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "DESIGN TEMPLATE")
public class DesignTemplatesController {
    private final DesignTemplatesService designTemplatesService;

    @PostMapping("/product-types/{productTypeId}/design-templates")
    @Operation(summary = "Tạo thiết kế mẫu theo loại sản phẩm")
    public ApiResponse<DesignTemplateDTO> createDesignTemplate(@PathVariable("productTypeId") String productTypeId,
                                                               @RequestBody DesignTemplateCreateRequest request) {
        var response = designTemplatesService.createDesignTemplate(productTypeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Create Design Template successful!", response);
    }

    @PatchMapping("/design-templates/{designTemplateId}/information")
    @Operation(summary = "Cập nhật lại thông tin thiết kế mẫu")
    public ApiResponse<DesignTemplateDTO> updateDesignTemplateInformation(@PathVariable String designTemplateId,
                                                                          @RequestBody DesignTemplateUpdateRequest request) {
        var response = designTemplatesService.updateDesignTemplateInformation(designTemplateId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update Design Template information successful!", response);
    }

    @PatchMapping(value = "/design-templates/{designTemplateId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cập nhật hình ảnh thiết kế mẫu")
    public ApiResponse<DesignTemplateDTO> updateDesignTemplateImage(@PathVariable String designTemplateId,
                                                                    @RequestPart("file") MultipartFile designTemplateImage) {
        var response = designTemplatesService.uploadDesignTemplateImage(designTemplateId, designTemplateImage);
        return ApiResponseBuilder.buildSuccessResponse("Update Design Template image successful!", response);
    }

    @GetMapping("/design-templates/{designTemplateId}")
    @Operation(summary = "Xem thiết kễ mẫu theo ID")
    public ApiResponse<DesignTemplateDTO> findDesignTemplateById(@PathVariable String designTemplateId) {
        var response = designTemplatesService.findDesignTemplateById(designTemplateId);
        return ApiResponseBuilder.buildSuccessResponse("Find Design Template successful!", response);
    }

    @GetMapping("product-types/{productTypeId}/design-templates")
    @Operation(summary = "Xem thiết kế mẫu theo loại sản phẩm")
    public ApiPagingResponse<DesignTemplateDTO> findDesignTemplateByProductTypeId(@PathVariable String productTypeId,
                                                                                  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = designTemplatesService.findDesignTemplateByProductTypeId(productTypeId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find Design Template by ProductTypeId successful!", response, page);
    }

    @GetMapping("/design-templates")
    @Operation(summary = "Xem tất cả các thiết kế mẫu")
    public ApiPagingResponse<DesignTemplateDTO> findAllDesignTemplates(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                       @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = designTemplatesService.findAllDesignTemplates(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find All Design Template successful!", response, page);
    }

    @DeleteMapping("/design-templates/{designTemplateId}")
    @Operation(summary = "Xóa cứng thiết kế mẫu")
    public ApiResponse<Void> hardDeleteDesignTemplate(@PathVariable String designTemplateId) {
        designTemplatesService.hardDeleteDesignTemplate(designTemplateId);
        return ApiResponseBuilder.buildSuccessResponse("Hard hardDeleteAttribute Design Template successful!", null);
    }
}
