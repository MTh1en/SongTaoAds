package com.capstone.ads.mapper;

import com.capstone.ads.dto.producttypesize.ProductTypeSizeDTO;
import com.capstone.ads.model.ProductType;
import com.capstone.ads.model.ProductTypeSize;
import com.capstone.ads.model.Size;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductTypeSizeMapper {
    @Mapping(target = "productTypeId", source = "productType.id")
    ProductTypeSizeDTO toDTO(ProductTypeSize productTypeSize);

    @Mapping(target = "productType", expression = "java(mapProductType(productTypeId))")
    @Mapping(target = "size", expression = "java(mapSize(sizeId))")
    ProductTypeSize toEntity(String productTypeId, String sizeId);

    default ProductType mapProductType(String productTypeId) {
        if (productTypeId == null) return null;
        ProductType productType = new ProductType();
        productType.setId(productTypeId);
        return productType;
    }

    default Size mapSize(String sizeId) {
        if (sizeId == null) return null;
        Size size = new Size();
        size.setId(sizeId);
        return size;
    }
}
