package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.producttypesize.ProductTypeSizeDTO;
import com.capstone.ads.service.ProductTypeSizeService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductTypeSizeController {
    private final ProductTypeSizeService service;

    @PostMapping("/product-types/{productTypeId}/sizes/{sizeId}")
    public ApiResponse<ProductTypeSizeDTO> create(@PathVariable String productTypeId, @PathVariable String sizeId) {
        return ApiResponseBuilder.buildSuccessResponse("Create product type successful", service.create(productTypeId, sizeId));
    }

    @GetMapping("product-types/{productTypeId}/product-type-sizes")
    public ApiResponse<List<ProductTypeSizeDTO>> getAllByProductTypeId(@PathVariable String productTypeId) {
        return ApiResponseBuilder.buildSuccessResponse("Find all product type size by product type", service.getAllByProductTypeId(productTypeId));
    }

    @DeleteMapping("/product-type-sizes/{productTypeSizeId}")
    public ApiResponse<Void> delete(@PathVariable String productTypeSizeId) {
        service.delete(productTypeSizeId);
        return ApiResponseBuilder.buildSuccessResponse("Delete product type size successful", null);
    }
}
