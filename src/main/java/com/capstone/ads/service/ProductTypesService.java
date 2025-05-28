package com.capstone.ads.service;

import com.capstone.ads.dto.producttype.ProductTypeCreateRequest;
import com.capstone.ads.dto.producttype.ProductTypeDTO;
import com.capstone.ads.dto.producttype.ProductTypeUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductTypesService {
    ProductTypeDTO createProductTypeService(ProductTypeCreateRequest request);

    ProductTypeDTO updateInformation(String productTypeId, ProductTypeUpdateRequest request);

    ProductTypeDTO findProductTypeByProductTypeId(String productTypeId);

    List<ProductTypeDTO> findAllProductType();

    void forceDeleteProductType(String productTypeId);

    ProductTypeDTO uploadProductTypeImage(String productTypeId, MultipartFile file);
}
