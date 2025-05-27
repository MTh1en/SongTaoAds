package com.capstone.ads.mapper;

import com.capstone.ads.dto.attribute.AttributesCreateRequest;
import com.capstone.ads.dto.attribute.AttributesDTO;
import com.capstone.ads.dto.attribute.AttributesUpdateRequest;
import com.capstone.ads.model.Attributes;
import com.capstone.ads.model.ProductTypes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AttributesMapper {
    @Mapping(target = "productTypeId", source = "productTypes.id")
    AttributesDTO toDTO(Attributes attributes);

    @Mapping(target = "productTypes", expression = "java(mapProductType(productId))")
    Attributes toEntity(String productId, AttributesCreateRequest request);

    void updateEntityFromRequest(AttributesUpdateRequest request, @MappingTarget Attributes attributes);

    default ProductTypes mapProductType(String productTypeId) {
        if (productTypeId == null) return null;
        ProductTypes productTypes = new ProductTypes();
        productTypes.setId(productTypeId);
        return productTypes;
    }
}
