package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.attribute.AttributesCreateRequest;
import com.capstone.ads.dto.attribute.AttributesDTO;
import com.capstone.ads.dto.attribute.AttributesUpdateRequest;
import com.capstone.ads.service.AttributesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "ATTRIBUTE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributesController {
    AttributesService service;

    @PostMapping("product-types/{productTypeId}/attributes")
    @Operation(summary = "Tạo thuộc tính theo loại biển")
    public ApiResponse<AttributesDTO> createAttribute(@Valid @PathVariable String productTypeId,
                                                      @RequestBody AttributesCreateRequest request) {
        var response = service.createAttribute(productTypeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Create attribute successful", response);
    }

    @PutMapping("/attributes/{attributeId}")
    @Operation(summary = "Cập nhật thuộc tính")
    public ApiResponse<AttributesDTO> updateAttributeInformation(@Valid @PathVariable String attributeId,
                                                                 @RequestBody AttributesUpdateRequest request) {
        var response = service.updateAttributeInformation(attributeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update attribute successful", response);
    }

    @GetMapping("/attributes/{attributeId}")
    @Operation(summary = "Xem thuộc tính theo ID")
    public ApiResponse<AttributesDTO> findAttributeById(@PathVariable String attributeId) {
        var response = service.findAttributeById(attributeId);
        return ApiResponseBuilder.buildSuccessResponse("attribute by Id", response);
    }

    @GetMapping("product-types/{productTypeId}/attributes")
    @Operation(summary = "Xem thuộc tính theo loại sản phẩm")
    public ApiPagingResponse<AttributesDTO> findAllAttributeByProductTypeId(
            @PathVariable String productTypeId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findAllAttributeByProductTypeId(productTypeId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find all attribute by product type", response, page);
    }

    @DeleteMapping("/attributes/{attributeId}")
    @Operation(summary = "Xóa cứng thuộc tính (Không dùng)")
    public ApiResponse<Void> delete(@PathVariable String attributeId) {
        service.hardDeleteAttribute(attributeId);
        return ApiResponseBuilder.buildSuccessResponse("Delete attribute successful", null);
    }
}
