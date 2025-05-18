package com.capstone.ads.service;

import com.capstone.ads.dto.producttype.ProductTypeCreateRequest;
import com.capstone.ads.dto.producttype.ProductTypeDTO;
import com.capstone.ads.dto.producttype.ProductTypeUpdateRequest;

import java.util.List;

public interface ProductTypeService {
    ProductTypeDTO create(ProductTypeCreateRequest request);
    ProductTypeDTO update(String id, ProductTypeUpdateRequest request);
    ProductTypeDTO findById(String id);
    List<ProductTypeDTO> findAll();
    void delete(String id);
}
