package com.capstone.ads.mapper;

import com.capstone.ads.dto.product_type_size.ProductTypeSizeCreateRequest;
import com.capstone.ads.dto.product_type_size.ProductTypeSizeDTO;
import com.capstone.ads.dto.product_type_size.ProductTypeSizeUpdateRequest;
import com.capstone.ads.model.ProductTypeSizes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductTypeSizesMapper {
    ProductTypeSizeDTO toDTO(ProductTypeSizes productTypeSizes);

    ProductTypeSizes mapCreateRequestToEntity(ProductTypeSizeCreateRequest request);

    void mapUpdateRequestToEntity(ProductTypeSizeUpdateRequest request, @MappingTarget ProductTypeSizes productTypeSizes);
}
