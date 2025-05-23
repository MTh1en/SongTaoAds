package com.capstone.ads.service;

import com.capstone.ads.dto.producttype.ProductTypeCreateRequest;
import com.capstone.ads.dto.producttype.ProductTypeDTO;
import com.capstone.ads.dto.producttype.ProductTypeUpdateRequest;

import java.util.List;

public interface ProductTypeService {
    ProductTypeDTO create(ProductTypeCreateRequest request);
    ProductTypeDTO update(String productTypeId, ProductTypeUpdateRequest request);
    ProductTypeDTO findById(String productTypeId);
    List<ProductTypeDTO> findAll();
    void delete(String productTypeId);
}
