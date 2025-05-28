package com.capstone.ads.service;

import com.capstone.ads.dto.producttypesize.ProductTypeSizeDTO;

import java.util.List;

public interface ProductTypeSizesService {
    ProductTypeSizeDTO create(String productTypeId, String sizeId);

    List<ProductTypeSizeDTO> getAllByProductTypeId(String productTypeId);

    void delete(String productTypeSizeId);
}
