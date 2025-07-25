package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.attribute_value.AttributeValuesCreateRequest;
import com.capstone.ads.dto.attribute_value.AttributeValuesDTO;
import com.capstone.ads.dto.attribute_value.AttributeValuesUpdateRequest;
import com.capstone.ads.service.AttributeValuesService;
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
@Tag(name = "ATTRIBUTE VALUE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeValuesController {
    AttributeValuesService service;

    @PostMapping("/attributes/{attributeId}/attribute-values")
    @Operation(summary = "Tạo giá trị thuộc tính")
    public ApiResponse<AttributeValuesDTO> createAttributeValue(@PathVariable String attributeId,
                                                                @Valid @RequestBody AttributeValuesCreateRequest request) {
        var response = service.createAttributeValue(attributeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Create attribute value successful", response);
    }

    @PutMapping("/attribute-values/{attributeValueId}")
    @Operation(summary = "Cập nhật lại giá trị thuộc tính")
    public ApiResponse<AttributeValuesDTO> updateAttributeValueInformation(@PathVariable String attributeValueId,
                                                                           @Valid @RequestBody AttributeValuesUpdateRequest request) {
        var response = service.updateAttributeValueInformation(attributeValueId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update attribute value successful", response);
    }

    @GetMapping("/attribute-values/{attributeValueId}")
    @Operation(summary = "Xem giá trị thuộc tính theo ID")
    public ApiResponse<AttributeValuesDTO> findAttributeValueById(@PathVariable String attributeValueId) {
        var response = service.findAttributeValueById(attributeValueId);
        return ApiResponseBuilder.buildSuccessResponse("attribute value by Id", response);
    }

    @GetMapping("/attributes/{attributeId}/attribute-values")
    @Operation(summary = "Xem giá trị theo từng thuộc tính")
    public ApiPagingResponse<AttributeValuesDTO> findAllAttributeValueByAttributesId(
            @PathVariable String attributeId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findAllAttributeValueByAttributesId(attributeId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find all attribute value by attribute", response, page);
    }

    @DeleteMapping("/attribute-values/{attributeValueId}")
    @Operation(summary = "Xóa cứng giá trị thuộc tính (Không dùng)")
    public ApiResponse<Void> hardDeleteAttributeValue(@PathVariable String attributeValueId) {
        service.hardDeleteAttributeValue(attributeValueId);
        return ApiResponseBuilder.buildSuccessResponse("Delete attribute value successful", null);
    }
}
