package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.attributevalue.AttributeValuesCreateRequest;
import com.capstone.ads.dto.attributevalue.AttributeValuesDTO;
import com.capstone.ads.dto.attributevalue.AttributeValuesUpdateRequest;
import com.capstone.ads.service.AttributeValuesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AttributeValuesController {
    private final AttributeValuesService service;

    @PostMapping("/attributes/{attributeId}/attribute-values")
    public ApiResponse<AttributeValuesDTO> create(@Valid @PathVariable String attributeId,
                                                  @RequestBody AttributeValuesCreateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Create attribute value successful", service.create(attributeId, request));
    }

    @PutMapping("/attribute-values/{attributeValueId}")
    public ApiResponse<AttributeValuesDTO> update(@Valid @PathVariable String attributeValueId,
                                                  @RequestBody AttributeValuesUpdateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Update attribute value successful", service.update(attributeValueId, request));
    }

    @GetMapping("/attribute-values/{attributeValueId}")
    public ApiResponse<AttributeValuesDTO> getById(@PathVariable String attributeValueId) {
        return ApiResponseBuilder.buildSuccessResponse("attribute value by Id", service.findById(attributeValueId));
    }

    @GetMapping("/attributes/{attributeId}/attribute-values")
    public ApiResponse<List<AttributeValuesDTO>> getAllByAttributes(@PathVariable String attributeId) {
        return ApiResponseBuilder.buildSuccessResponse("Find all attribute value by attribute", service.findAllByAttributesId(attributeId));
    }

    @DeleteMapping("/attribute-values/{attributeValueId}")
    public ApiResponse<Void> delete(@PathVariable String attributeValueId) {
        service.delete(attributeValueId);
        return ApiResponseBuilder.buildSuccessResponse("Delete attribute value successful", null);
    }
}
