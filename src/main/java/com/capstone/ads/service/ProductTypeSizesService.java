package com.capstone.ads.service;

import com.capstone.ads.dto.product_type_size.ProductTypeSizeDTO;

import java.util.List;

public interface ProductTypeSizesService {
    ProductTypeSizeDTO createProductTypeSize(String productTypeId, String sizeId);

    List<ProductTypeSizeDTO> findAllProductTypeSizeByProductTypeId(String productTypeId);

    void hardDeleteProductTypeSize(String productTypeSizeId);

    //INTERNAL FUNCTION
    void validateProductTypeSizeExist(String productTypeId, String sizeId);

    void validateProductTypeSizeMaxValueAndMinValue(String productTypeId, String sizeId, Float sizeValue);
}
