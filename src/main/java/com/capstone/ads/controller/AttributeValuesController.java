package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
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
        var response = service.create(attributeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Create attribute value successful", response);
    }

    @PutMapping("/attribute-values/{attributeValueId}")
    public ApiResponse<AttributeValuesDTO> update(@Valid @PathVariable String attributeValueId,
                                                  @RequestBody AttributeValuesUpdateRequest request) {
        var response = service.update(attributeValueId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update attribute value successful", response);
    }

    @GetMapping("/attribute-values/{attributeValueId}")
    public ApiResponse<AttributeValuesDTO> getById(@PathVariable String attributeValueId) {
        var response = service.findById(attributeValueId);
        return ApiResponseBuilder.buildSuccessResponse("attribute value by Id", response);
    }

    @GetMapping("/attributes/{attributeId}/attribute-values")
    public ApiPagingResponse<AttributeValuesDTO> getAllByAttributes(@PathVariable String attributeId,
                                                                    @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findAllByAttributesId(attributeId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find all attribute value by attribute", response, page);
    }

    @DeleteMapping("/attribute-values/{attributeValueId}")
    public ApiResponse<Void> delete(@PathVariable String attributeValueId) {
        service.delete(attributeValueId);
        return ApiResponseBuilder.buildSuccessResponse("Delete attribute value successful", null);
    }
}
