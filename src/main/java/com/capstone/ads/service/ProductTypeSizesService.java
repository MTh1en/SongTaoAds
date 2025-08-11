package com.capstone.ads.service;

import com.capstone.ads.dto.product_type_size.ProductTypeSizeCreateRequest;
import com.capstone.ads.dto.product_type_size.ProductTypeSizeDTO;
import com.capstone.ads.dto.product_type_size.ProductTypeSizeUpdateRequest;
import com.capstone.ads.model.ProductTypeSizes;

import java.util.List;

public interface ProductTypeSizesService {
    ProductTypeSizeDTO createProductTypeSize(String productTypeId, String sizeId, ProductTypeSizeCreateRequest request);

    ProductTypeSizeDTO updateProductTypeSize(String productTypeSizeId, ProductTypeSizeUpdateRequest request);

    List<ProductTypeSizeDTO> findAllProductTypeSizeByProductTypeId(String productTypeId);

    void hardDeleteProductTypeSize(String productTypeSizeId);

    //INTERNAL FUNCTION
    void validateProductTypeSizeExist(String productTypeId, String sizeId);

    void validateProductTypeSizeMaxValueAndMinValue(String productTypeId, String sizeId, Float sizeValue);

    ProductTypeSizes getProductTypeSizeByProductTypeIdAndSizeId(String productTypeId, String sizeId);
}
