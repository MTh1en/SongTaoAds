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

import java.util.List;


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
        return ApiResponseBuilder.buildSuccessResponse("Tạo thuộc tính thành công", response);
    }

    @PutMapping("/attributes/{attributeId}")
    @Operation(summary = "Cập nhật thuộc tính")
    public ApiResponse<AttributesDTO> updateAttributeInformation(@Valid @PathVariable String attributeId,
                                                                 @RequestBody AttributesUpdateRequest request) {
        var response = service.updateAttributeInformation(attributeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Câp nhật thuộc tính thành công", response);
    }

    @GetMapping("/attributes/{attributeId}")
    @Operation(summary = "Xem thuộc tính theo ID")
    public ApiResponse<AttributesDTO> findAttributeById(@PathVariable String attributeId) {
        var response = service.findAttributeById(attributeId);
        return ApiResponseBuilder.buildSuccessResponse("Xem thuộc tính theo ID", response);
    }

    @GetMapping(value = "product-types/{productTypeId}/attributes", params = "!isAvailable")
    @Operation(summary = "Xem thuộc tính theo loại biển hiệu")
    public ApiResponse<List<AttributesDTO>> findAllAttributeByProductTypeId(
            @PathVariable String productTypeId) {
        var response = service.findAllAttributeByProductTypeId(productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Xem tất cả thuộc tính theo loại biển hiệu", response);
    }

    @GetMapping(value = "product-types/{productTypeId}/attributes", params = "isAvailable")
    @Operation(summary = "Xem thuộc tính theo loại biển hiệu")
    public ApiResponse<List<AttributesDTO>> findAllAttributeByProductTypeIdAndIsAvailable(
            @PathVariable String productTypeId,
            @RequestParam(required = false) boolean isAvailable) {
        var response = service.findAllAttributeByProductTypeIdAndIsAvailable(productTypeId, isAvailable);
        return ApiResponseBuilder.buildSuccessResponse(
                "Xem tất cả thuộc tính theo loại biển hiệu và có sẵn hay không",
                response);
    }

    @DeleteMapping("/attributes/{attributeId}")
    @Operation(summary = "Xóa cứng thuộc tính (Không dùng)")
    public ApiResponse<Void> delete(@PathVariable String attributeId) {
        service.hardDeleteAttribute(attributeId);
        return ApiResponseBuilder.buildSuccessResponse("Xóa thuộc tính thành công", null);
    }
}
