package com.capstone.ads.controller;

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

import java.util.List;

@RestController
@RequestMapping("/api/product-types")
@RequiredArgsConstructor
public class ProductTypeController {
    private final ProductTypesService service;

    @PostMapping
    public ApiResponse<ProductTypeDTO> createProductType(@RequestBody ProductTypeCreateRequest request) {
        var response = service.createProductTypeService(request);
        return ApiResponseBuilder.buildSuccessResponse("Create product type successful", response);
    }

    @PutMapping("/{productTypeId}/information")
    public ApiResponse<ProductTypeDTO> updateProductTypeInformation(@PathVariable String productTypeId,
                                                                    @RequestBody ProductTypeUpdateRequest request) {
        var response = service.updateInformation(productTypeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update product type information successful", response);
    }

    @PutMapping(value = "/{productTypeId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductTypeDTO> updateProductTypeImage(@PathVariable String productTypeId,
                                                              @RequestParam("file") MultipartFile productTypeImage) {
        var response = service.uploadProductTypeImage(productTypeId, productTypeImage);
        return ApiResponseBuilder.buildSuccessResponse("Update product type image successful", response);
    }

    @GetMapping("/{productTypeId}")
    public ApiResponse<ProductTypeDTO> findProductTypeByProductTypeId(@PathVariable String productTypeId) {
        var response = service.findProductTypeByProductTypeId(productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Product Type by Id", response);
    }

    @GetMapping
    public ApiResponse<List<ProductTypeDTO>> findAllProductType() {
        var response = service.findAllProductType();
        return ApiResponseBuilder.buildSuccessResponse("Find all product type", response);
    }

    @DeleteMapping("/{productTypeId}")
    public ApiResponse<Void> delete(@PathVariable String productTypeId) {
        service.forceDeleteProductType(productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Delete product type successful", null);
    }
}
