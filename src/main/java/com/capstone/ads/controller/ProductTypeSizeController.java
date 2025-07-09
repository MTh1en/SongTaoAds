package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.product_type_size.ProductTypeSizeDTO;
import com.capstone.ads.service.ProductTypeSizesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "PRODUCT TYPE SIZE")
public class ProductTypeSizeController {
    private final ProductTypeSizesService service;

    @PostMapping("/product-types/{productTypeId}/sizes/{sizeId}")
    @Operation(summary = "Tạo kích thước sử dụng trong loại biển")
    public ApiResponse<ProductTypeSizeDTO> createProductTypeSize(@PathVariable String productTypeId,
                                                                 @PathVariable String sizeId) {
        var response = service.createProductTypeSize(productTypeId, sizeId);
        return ApiResponseBuilder.buildSuccessResponse("Create product type successful", response);
    }

    @GetMapping("product-types/{productTypeId}/product-type-sizes")
    @Operation(summary = "Xem kích thước mà biển đó cần dùng")
    public ApiResponse<List<ProductTypeSizeDTO>> getAllProductTypeSizeByProductTypeId(@PathVariable String productTypeId) {
        var response = service.findAllProductTypeSizeByProductTypeId(productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Find all product type size by product type", response);
    }

    @DeleteMapping("/product-type-sizes/{productTypeSizeId}")
    @Operation(summary = "Xóa cứng kích thước cần dùng khỏi biển")
    public ApiResponse<Void> hardDeleteProductTypeSize(@PathVariable String productTypeSizeId) {
        service.hardDeleteProductTypeSize(productTypeSizeId);
        return ApiResponseBuilder.buildSuccessResponse("Delete product type size successful", null);
    }
}
