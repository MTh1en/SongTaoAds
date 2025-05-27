package com.capstone.ads.mapper;

import com.capstone.ads.dto.producttypesize.ProductTypeSizeDTO;
import com.capstone.ads.model.ProductTypeSizes;
import com.capstone.ads.model.ProductTypes;
import com.capstone.ads.model.Sizes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductTypeSizesMapper {
    @Mapping(target = "productTypeId", source = "productTypes.id")
    ProductTypeSizeDTO toDTO(ProductTypeSizes productTypeSizes);

    @Mapping(target = "productTypes", expression = "java(mapProductType(productTypeId))")
    @Mapping(target = "sizes", expression = "java(mapSize(sizeId))")
    ProductTypeSizes toEntity(String productTypeId, String sizeId);

    default ProductTypes mapProductType(String productTypeId) {
        if (productTypeId == null) return null;
        ProductTypes productTypes = new ProductTypes();
        productTypes.setId(productTypeId);
        return productTypes;
    }

    default Sizes mapSize(String sizeId) {
        if (sizeId == null) return null;
        Sizes sizes = new Sizes();
        sizes.setId(sizeId);
        return sizes;
    }
}
