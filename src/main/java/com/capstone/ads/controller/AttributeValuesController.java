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

import java.util.List;

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
        return ApiResponseBuilder.buildSuccessResponse("Tạo giá trị thuộc tính thành công", response);
    }

    @PutMapping("/attribute-values/{attributeValueId}")
    @Operation(summary = "Cập nhật lại giá trị thuộc tính")
    public ApiResponse<AttributeValuesDTO> updateAttributeValueInformation(@PathVariable String attributeValueId,
                                                                           @Valid @RequestBody AttributeValuesUpdateRequest request) {
        var response = service.updateAttributeValueInformation(attributeValueId, request);
        return ApiResponseBuilder.buildSuccessResponse("Cập nhật giá trị thuộc tính thành công", response);
    }

    @GetMapping("/attribute-values/{attributeValueId}")
    @Operation(summary = "Xem giá trị thuộc tính theo ID")
    public ApiResponse<AttributeValuesDTO> findAttributeValueById(@PathVariable String attributeValueId) {
        var response = service.findAttributeValueById(attributeValueId);
        return ApiResponseBuilder.buildSuccessResponse("Xem giá trị thuộc tính theo ID", response);
    }

    @GetMapping(value = "/attributes/{attributeId}/attribute-values", params = "!isAvailable")
    @Operation(summary = "Xem giá trị theo từng thuộc tính")
    public ApiResponse<List<AttributeValuesDTO>> findAllAttributeValueByAttributesId(
            @PathVariable String attributeId) {
        var response = service.findAllAttributeValueByAttributesId(attributeId);
        return ApiResponseBuilder.buildSuccessResponse(
                "Xem tất cả giá trị thuộc tính theo thuộc tính",
                response);
    }

    @GetMapping(value = "/attributes/{attributeId}/attribute-values", params = "isAvailable")
    @Operation(summary = "Xem giá trị theo từng thuộc tính và có sẵn hay không")
    public ApiResponse<List<AttributeValuesDTO>> findAllAttributeValueByAttributesId(
            @PathVariable String attributeId,
            @RequestParam(required = false) boolean isAvailable) {
        var response = service.findAllAttributeValueByAttributeIdAndIsAvailable(attributeId, isAvailable);
        return ApiResponseBuilder.buildSuccessResponse(
                "Xem tất cả giá trị thuộc tính theo thuộc tính và có sẵn hay không",
                response);
    }

    @DeleteMapping("/attribute-values/{attributeValueId}")
    @Operation(summary = "Xóa cứng giá trị thuộc tính (Không dùng)")
    public ApiResponse<Void> hardDeleteAttributeValue(@PathVariable String attributeValueId) {
        service.hardDeleteAttributeValue(attributeValueId);
        return ApiResponseBuilder.buildSuccessResponse("Xóa giá trị thuộc tính thành công", null);
    }
}
