package com.capstone.ads.mapper;

import com.capstone.ads.dto.product_type_size.ProductTypeSizeDTO;
import com.capstone.ads.model.ProductTypeSizes;
import com.capstone.ads.model.ProductTypes;
import com.capstone.ads.model.Sizes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductTypeSizesMapper {
    ProductTypeSizeDTO toDTO(ProductTypeSizes productTypeSizes);
}
