package com.capstone.ads.mapper;

import com.capstone.ads.dto.producttype.ProductTypeCreateRequest;
import com.capstone.ads.dto.producttype.ProductTypeDTO;
import com.capstone.ads.dto.producttype.ProductTypeUpdateRequest;
import com.capstone.ads.model.ProductType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {
    ProductTypeDTO toDTO(ProductType productType);

    ProductType toEntity(ProductTypeCreateRequest request);

    void updateEntityFromRequest(ProductTypeUpdateRequest request, @MappingTarget ProductType productType);
}
