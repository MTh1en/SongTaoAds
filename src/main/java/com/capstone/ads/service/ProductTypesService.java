package com.capstone.ads.service;

import com.capstone.ads.dto.product_type.ProductTypeCreateRequest;
import com.capstone.ads.dto.product_type.ProductTypeDTO;
import com.capstone.ads.dto.product_type.ProductTypeUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ProductTypesService {
    ProductTypeDTO createProductType(ProductTypeCreateRequest request);

    ProductTypeDTO updateProductTypeInformation(String productTypeId, ProductTypeUpdateRequest request);

    ProductTypeDTO findProductTypeByProductTypeId(String productTypeId);

    Page<ProductTypeDTO> findAllProductType(int page, int size);

    void hardDeleteProductType(String productTypeId);

    ProductTypeDTO uploadProductTypeImage(String productTypeId, MultipartFile file);

    //INTERNAL FUNCTION
    void validateProductTypeExistsAndAvailable(String productTypeId);
}
