package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.producttype.ProductTypeCreateRequest;
import com.capstone.ads.dto.producttype.ProductTypeDTO;
import com.capstone.ads.dto.producttype.ProductTypeUpdateRequest;
import com.capstone.ads.service.ProductTypesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/product-types")
@RequiredArgsConstructor
public class ProductTypeController {
    private final ProductTypesService service;

    @PostMapping
    public ApiResponse<ProductTypeDTO> createProductType(@RequestBody ProductTypeCreateRequest request) {
        var response = service.createProductType(request);
        return ApiResponseBuilder.buildSuccessResponse("Create product type successful", response);
    }

    @PatchMapping("/{productTypeId}/information")
    public ApiResponse<ProductTypeDTO> updateProductTypeInformation(@PathVariable String productTypeId,
                                                                    @RequestBody ProductTypeUpdateRequest request) {
        var response = service.updateProductTypeInformation(productTypeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update product type information successful", response);
    }

    @PatchMapping(value = "/{productTypeId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductTypeDTO> uploadProductTypeImage(@PathVariable String productTypeId,
                                                              @RequestPart("file") MultipartFile productTypeImage) {
        var response = service.uploadProductTypeImage(productTypeId, productTypeImage);
        return ApiResponseBuilder.buildSuccessResponse("Update product type image successful", response);
    }

    @GetMapping("/{productTypeId}")
    public ApiResponse<ProductTypeDTO> findProductTypeByProductTypeId(@PathVariable String productTypeId) {
        var response = service.findProductTypeByProductTypeId(productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Product Type by Id", response);
    }

    @GetMapping
    public ApiPagingResponse<ProductTypeDTO> findAllProductType(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findAllProductType(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find all product type", response, page);
    }

    @DeleteMapping("/{productTypeId}")
    public ApiResponse<Void> hardDeleteProductType(@PathVariable String productTypeId) {
        service.hardDeleteProductType(productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Delete product type successful", null);
    }
}
