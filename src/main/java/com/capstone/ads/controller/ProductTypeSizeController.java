package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.product_type_size.ProductTypeSizeCreateRequest;
import com.capstone.ads.dto.product_type_size.ProductTypeSizeDTO;
import com.capstone.ads.dto.product_type_size.ProductTypeSizeUpdateRequest;
import com.capstone.ads.service.ProductTypeSizesService;
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
@Tag(name = "PRODUCT TYPE SIZE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductTypeSizeController {
    ProductTypeSizesService service;

    @PostMapping("/product-types/{productTypeId}/sizes/{sizeId}")
    @Operation(summary = "Tạo kích thước sử dụng trong loại biển")
    public ApiResponse<ProductTypeSizeDTO> createProductTypeSize(@PathVariable String productTypeId,
                                                                 @PathVariable String sizeId,
                                                                 @Valid @RequestBody ProductTypeSizeCreateRequest request) {
        var response = service.createProductTypeSize(productTypeId, sizeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Tạo kích thước sử dụng trong loại biển thành công", response);
    }

    @PatchMapping("/product-type-sizes/{productTypeSizeId}")
    @Operation(summary = "Tạo kích thước sử dụng trong loại biển")
    public ApiResponse<ProductTypeSizeDTO> createProductTypeSize(@PathVariable String productTypeSizeId,
                                                                 @Valid @RequestBody ProductTypeSizeUpdateRequest request) {
        var response = service.updateProductTypeSize(productTypeSizeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Tạo kích thước sử dụng trong loại biển thành công", response);
    }

    @GetMapping("product-types/{productTypeId}/product-type-sizes")
    @Operation(summary = "Xem kích thước mà biển đó cần dùng")
    public ApiResponse<List<ProductTypeSizeDTO>> getAllProductTypeSizeByProductTypeId(@PathVariable String productTypeId) {
        var response = service.findAllProductTypeSizeByProductTypeId(productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Xem kích thước mà biển đó cần dùng thành công", response);
    }

    @DeleteMapping("/product-type-sizes/{productTypeSizeId}")
    @Operation(summary = "Xóa cứng kích thước cần dùng khỏi biển")
    public ApiResponse<Void> hardDeleteProductTypeSize(@PathVariable String productTypeSizeId) {
        service.hardDeleteProductTypeSize(productTypeSizeId);
        return ApiResponseBuilder.buildSuccessResponse("Xóa cứng kích thước cần dùng khỏi biển thành công", null);
    }
}
