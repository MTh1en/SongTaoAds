package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.attribute.AttributesCreateRequest;
import com.capstone.ads.dto.attribute.AttributesDTO;
import com.capstone.ads.dto.attribute.AttributesUpdateRequest;
import com.capstone.ads.service.AttributesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AttributesController {
    private final AttributesService service;

    @PostMapping("product-types/{productTypeId}/attributes")
    public ApiResponse<AttributesDTO> create(@PathVariable String productTypeId,
                                             @RequestBody AttributesCreateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Create attribute successful", service.create(productTypeId, request));
    }

    @PutMapping("/attributes/{attributeId}")
    public ApiResponse<AttributesDTO> update(@PathVariable String attributeId,
                                                @RequestBody AttributesUpdateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Update attribute successful", service.update(attributeId, request));
    }

    @GetMapping("/attributes/{attributeId}")
    public ApiResponse<AttributesDTO> getById(@PathVariable String attributeId) {
        return ApiResponseBuilder.buildSuccessResponse("attribute by Id", service.findById(attributeId));
    }

    @GetMapping("product-types/{productTypeId}/attributes")
    public ApiResponse<List<AttributesDTO>> getAll(@PathVariable String productTypeId) {
        return ApiResponseBuilder.buildSuccessResponse("Find all attribute by product type", service.findAllByProductTypeId(productTypeId));
    }

    @DeleteMapping("/attributes/{attributeId}")
    public ApiResponse<Void> delete(@PathVariable String attributeId) {
        service.delete(attributeId);
        return ApiResponseBuilder.buildSuccessResponse("Delete attribute successful", null);
    }
}
