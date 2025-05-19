package com.capstone.ads.mapper;

import com.capstone.ads.dto.customerchoice.CustomerChoicesCreateRequest;
import com.capstone.ads.dto.customerchoice.CustomerChoicesDTO;
import com.capstone.ads.dto.customerchoice.CustomerChoicesUpdateRequest;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.ProductType;
import com.capstone.ads.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerChoicesMapper {
    @Mapping(target = "userId", source = "users.id")
    @Mapping(target = "productTypeId", source = "productType.id")
    CustomerChoicesDTO toDTO(CustomerChoices customerChoices);

    @Mapping(target = "users", expression = "java(mapUsers(userId))")
    @Mapping(target = "productType", expression = "java(mapProductType(productTypeId))")
    CustomerChoices toEntity(CustomerChoicesCreateRequest request, String userId, String productTypeId);

    void updateEntityFromRequest(CustomerChoicesUpdateRequest request, @MappingTarget CustomerChoices customerChoices);

    default Users mapUsers(String userId) {
        if (userId == null) return null;
        Users users = new Users();
        users.setId(userId);
        return users;
    }

    default ProductType mapProductType(String productTypeId) {
        if (productTypeId == null) return null;
        ProductType productType = new ProductType();
        productType.setId(productTypeId);
        return productType;
    }
}
