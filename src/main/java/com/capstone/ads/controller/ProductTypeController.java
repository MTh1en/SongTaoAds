package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.product_type.ProductTypeCreateRequest;
import com.capstone.ads.dto.product_type.ProductTypeDTO;
import com.capstone.ads.dto.product_type.ProductTypeUpdateRequest;
import com.capstone.ads.service.ProductTypesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/product-types")
@RequiredArgsConstructor
@Tag(name = "PRODUCT TYPE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductTypeController {
    ProductTypesService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Tạo loại biển hiệu")
    public ApiResponse<ProductTypeDTO> createProductType(@Valid @ModelAttribute ProductTypeCreateRequest request) {
        var response = service.createProductType(request);
        return ApiResponseBuilder.buildSuccessResponse("Tạo loại biển hiệu thành công", response);
    }

    @PatchMapping("/{productTypeId}/information")
    @Operation(summary = "Cập nhập thông tin loại biển hiệu")
    public ApiResponse<ProductTypeDTO> updateProductTypeInformation(@PathVariable String productTypeId,
                                                                    @Valid @RequestBody ProductTypeUpdateRequest request) {
        var response = service.updateProductTypeInformation(productTypeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Cập nhật thông tin loại biển hiệu thành công", response);
    }

    @PatchMapping(value = "/{productTypeId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cập nhật hình ảnh đại diện của loại biển hiệu")
    public ApiResponse<ProductTypeDTO> uploadProductTypeImage(@PathVariable String productTypeId,
                                                              @RequestPart("file") MultipartFile productTypeImage) {
        var response = service.uploadProductTypeImage(productTypeId, productTypeImage);
        return ApiResponseBuilder.buildSuccessResponse("Cập nhật ảnh đại diện của loại biển hiệu thành công", response);
    }

    @GetMapping("/{productTypeId}")
    @Operation(summary = "Xem loại biển hiệu theo ID")
    public ApiResponse<ProductTypeDTO> findProductTypeByProductTypeId(@PathVariable String productTypeId) {
        var response = service.findProductTypeByProductTypeId(productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Xem loại biển hiệu theo ID", response);
    }

    @GetMapping(params = "!isAvailable")
    @Operation(summary = "Xem tất cả các loại sản phẩm")
    public ApiPagingResponse<ProductTypeDTO> findAllProductType(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findAllProductType(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem tất cả loại biển hiệu", response, page);
    }

    @GetMapping(params = "isAvailable")
    @Operation(summary = "Xem tất cả các loại sản phẩm")
    public ApiPagingResponse<ProductTypeDTO> findAllProductType(
            @RequestParam(required = false) boolean isAvailable,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findProductTypeByIsAvailable(isAvailable, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem biển hiệu theo isAvailable", response, page);
    }


    @DeleteMapping("/{productTypeId}")
    @Operation(summary = "Xóa cứng loại sản phẩm (Không dùng)")
    public ApiResponse<Void> hardDeleteProductType(@PathVariable String productTypeId) {
        service.hardDeleteProductType(productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Xóa loại biển hiệu thành công", null);
    }
}
