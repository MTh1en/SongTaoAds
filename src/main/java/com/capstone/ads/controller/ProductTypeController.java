package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
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

    @PutMapping("/{id}")
    public ApiResponse<ProductTypeDTO> update(@PathVariable("id") String id,
                                              @RequestBody ProductTypeUpdateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Update product type successful", service.update(id, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductTypeDTO> getById(@PathVariable String id) {
        return ApiResponseBuilder.buildSuccessResponse("Product Type by Id", service.findById(id));
    }

    @GetMapping
    public ApiResponse<List<ProductTypeDTO>> getAll() {
        return ApiResponseBuilder.buildSuccessResponse("Find all product type", service.findAll());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ApiResponseBuilder.buildSuccessResponse("Delete product type successful", null);
    }
}
