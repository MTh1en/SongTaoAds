package com.capstone.ads.mapper;

import com.capstone.ads.dto.product_type_size.ProductTypeSizeDTO;
import com.capstone.ads.model.ProductTypeSizes;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductTypeSizesMapper {
    ProductTypeSizeDTO toDTO(ProductTypeSizes productTypeSizes);
}
