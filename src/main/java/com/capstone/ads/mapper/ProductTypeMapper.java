package com.capstone.ads.mapper;

import com.capstone.ads.dto.producttype.ProductTypeCreateRequest;
import com.capstone.ads.dto.producttype.ProductTypeDTO;
import com.capstone.ads.dto.producttype.ProductTypeUpdateRequest;
import com.capstone.ads.model.ProductTypes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {
    ProductTypeDTO toDTO(ProductTypes productTypes);

    ProductTypes toEntity(ProductTypeCreateRequest request);

    void updateEntityFromRequest(ProductTypeUpdateRequest request, @MappingTarget ProductTypes productTypes);
}
