package com.capstone.ads.mapper;

import com.capstone.ads.dto.product_type.ProductTypeCreateRequest;
import com.capstone.ads.dto.product_type.ProductTypeDTO;
import com.capstone.ads.dto.product_type.ProductTypeUpdateRequest;
import com.capstone.ads.model.ProductTypes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductTypesMapper {
    ProductTypeDTO toDTO(ProductTypes productTypes);

    ProductTypes toEntity(ProductTypeCreateRequest request);

    void updateEntityFromRequest(ProductTypeUpdateRequest request, @MappingTarget ProductTypes productTypes);
}
