package com.capstone.ads.service;

import com.capstone.ads.dto.product_type.ProductTypeCreateRequest;
import com.capstone.ads.dto.product_type.ProductTypeDTO;
import com.capstone.ads.dto.product_type.ProductTypeUpdateRequest;
import com.capstone.ads.model.ProductTypes;
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
    ProductTypes getProductTypeByIdAndAvailable(String productTypeId);

    ProductTypes getProductTypeById(String productTypeId);

    void addCostTypeToCalculateFormula(String productTypeId, String costTypeName);

    void updateNewValueNameCalculateFormula(String productTypeId, String oldName, String newName);

    void updateAllFormulaCostTypeValues(String productTypeId, String oldValue, String newValue);
}
