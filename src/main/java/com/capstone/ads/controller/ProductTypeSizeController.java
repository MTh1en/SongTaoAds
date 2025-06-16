package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.producttypesize.ProductTypeSizeDTO;
import com.capstone.ads.service.ProductTypeSizesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductTypeSizeController {
    private final ProductTypeSizesService service;

    @PostMapping("/product-types/{productTypeId}/sizes/{sizeId}")
    public ApiResponse<ProductTypeSizeDTO> createProductTypeSize(@PathVariable String productTypeId, @PathVariable String sizeId) {
        var response = service.createProductTypeSize(productTypeId, sizeId);
        return ApiResponseBuilder.buildSuccessResponse("Create product type successful", response);
    }

    @GetMapping("product-types/{productTypeId}/product-type-sizes")
    public ApiResponse<List<ProductTypeSizeDTO>> getAllProductTypeSizeByProductTypeId(@PathVariable String productTypeId) {
        var response = service.findAllProductTypeSizeByProductTypeId(productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Find all product type size by product type", response);
    }

    @DeleteMapping("/product-type-sizes/{productTypeSizeId}")
    public ApiResponse<Void> hardDeleteProductTypeSize(@PathVariable String productTypeSizeId) {
        service.hardDeleteProductTypeSize(productTypeSizeId);
        return ApiResponseBuilder.buildSuccessResponse("Delete product type size successful", null);
    }
}
