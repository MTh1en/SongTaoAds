package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.attribute.AttributesDTO;
import com.capstone.ads.dto.producttype.ProductTypeCreateRequest;
import com.capstone.ads.dto.producttype.ProductTypeDTO;
import com.capstone.ads.dto.producttype.ProductTypeUpdateRequest;
import com.capstone.ads.service.ProductTypeService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-types")
@RequiredArgsConstructor
public class ProductTypeController {
    private final ProductTypeService service;

    @PostMapping
    public ApiResponse<ProductTypeDTO> create(@RequestBody ProductTypeCreateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Create product type successful", service.create(request));
    }

    @PutMapping("/{productTypeId}")
    public ApiResponse<ProductTypeDTO> update(@PathVariable String productTypeId,
                                              @RequestBody ProductTypeUpdateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Update product type successful", service.update(productTypeId, request));
    }

    @GetMapping("/{productTypeId}")
    public ApiResponse<ProductTypeDTO> getById(@PathVariable String productTypeId) {
        return ApiResponseBuilder.buildSuccessResponse("Product Type by Id", service.findById(productTypeId));
    }

    @GetMapping
    public ApiResponse<List<ProductTypeDTO>> getAll() {
        return ApiResponseBuilder.buildSuccessResponse("Find all product type", service.findAll());
    }

    @DeleteMapping("/{productTypeId}")
    public ApiResponse<Void> delete(@PathVariable String productTypeId) {
        service.delete(productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Delete product type successful", null);
    }
}
